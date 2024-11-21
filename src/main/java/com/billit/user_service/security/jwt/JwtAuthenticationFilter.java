package com.billit.user_service.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 디버깅을 위한 로그 추가
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Request Method: " + request.getMethod());
        System.out.println("Is Refresh Token Request: " + isRefreshTokenRequest(request));

        String token = resolveToken(request);

        try {
            if (isRefreshTokenRequest(request)) {
                if (token != null) {
                    if (jwtTokenProvider.validateRefreshToken(token)) {
                        String userEmail = jwtTokenProvider.getUserEmail(token);
                        Authentication authentication =
                                new UsernamePasswordAuthenticationToken(userEmail, null, new ArrayList<>());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } else {
                if (token != null && jwtTokenProvider.validateToken(token)) {
                    String userEmail = jwtTokenProvider.getUserEmail(token);
                    Authentication authentication =
                            new UsernamePasswordAuthenticationToken(userEmail, null, new ArrayList<>());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            System.out.println("Token validation failed: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private boolean isRefreshTokenRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return (uri.contains("/api/v1/user_service/users/borrow/refresh") ||
                uri.contains("/api/v1/user_service/users/invest/refresh")) &&
                "POST".equalsIgnoreCase(request.getMethod());
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
