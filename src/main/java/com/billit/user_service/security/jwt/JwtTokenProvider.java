package com.billit.user_service.security.jwt;

import com.billit.user_service.common.exception.CustomException;
import com.billit.user_service.common.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    private final long accessTokenValidityInMilliseconds = 1000 * 60 * 30; // 30분으로 변경
    private final long refreshTokenValidityInMilliseconds = 1000 * 60 * 60 * 24 * 14; // 2주

    private final RefreshTokenRepository refreshTokenRepository;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // 액세스 토큰 생성 (기존 createToken 메서드 이름 변경)
    public String createAccessToken(String userEmail, String role) {
        Claims claims = Jwts.claims().setSubject(userEmail);
        claims.put("role", role);

        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    // 리프레시 토큰 생성
    @Transactional(rollbackOn = Exception.class)
    public String createRefreshToken(String userEmail) {
        try {
            Date now = new Date();
            Date validity = new Date(now.getTime() + refreshTokenValidityInMilliseconds);

            String refreshToken = Jwts.builder()
                    .setSubject(userEmail)
                    .setIssuedAt(now)
                    .setExpiration(validity)
                    .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .compact();

            RefreshToken token = RefreshToken.builder()
                    .token(refreshToken)
                    .userEmail(userEmail)
                    .expiryDate(validity.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                    .build();

            refreshTokenRepository.save(token);
            return refreshToken;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_SAVE_FAILED);
        }
    }

    // 토큰에서 이메일 추출 (기존 유지)
    public String getUserEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 토큰 유효성 검증 (기존 유지)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 리프레시 토큰 검증
    public boolean validateRefreshToken(String token) {
        if (!validateToken(token)) {
            return false;
        }

        return refreshTokenRepository.findByToken(token)
                .map(refreshToken ->
                        !refreshToken.isRevoked() &&
                                refreshToken.getExpiryDate().isAfter(LocalDateTime.now())
                )
                .orElse(false);
    }

    // 리프레시 토큰으로 새 액세스 토큰 발급
    public String refreshAccessToken(String refreshToken) {
        if (!validateRefreshToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String userEmail = getUserEmail(refreshToken);
        return createAccessToken(userEmail, "ROLE_USER"); // 역할은 상황에 맞게 수정
    }

    // 리프레시 토큰 폐기 (로그아웃 시 사용)
    public void revokeRefreshToken(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }

    // JwtTokenProvider.java에 추가
    public String createVerificationToken(String userEmail) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 1000 * 60 * 30); // 30분 유효

        return Jwts.builder()
                .setSubject(userEmail)
                .setIssuedAt(now)
                .setExpiration(validity)
                .claim("type", "verification")
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    // 비밀번호 토큰
    public boolean validateVerificationToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return "verification".equals(claims.get("type"));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}