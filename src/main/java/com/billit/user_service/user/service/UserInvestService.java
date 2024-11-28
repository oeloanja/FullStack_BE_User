package com.billit.user_service.user.service;

import com.billit.user_service.account.domain.repository.InvestAccountRepository;
import com.billit.user_service.account.domain.repository.InvestAccountRepository;
import com.billit.user_service.account.dto.response.AccountInvestResponse;
import com.billit.user_service.account.dto.response.AccountInvestResponse;
import com.billit.user_service.common.exception.CustomException;
import com.billit.user_service.common.exception.ErrorCode;
import com.billit.user_service.common.service.EmailService;
import com.billit.user_service.security.jwt.JwtTokenProvider;
import com.billit.user_service.user.domain.entity.EmailVerification;
import com.billit.user_service.user.domain.entity.UserInvest;
import com.billit.user_service.user.domain.entity.UserInvest;
import com.billit.user_service.user.domain.repository.EmailVerificationRepository;
import com.billit.user_service.user.domain.repository.MasterCodeRepository;
import com.billit.user_service.user.domain.repository.UserInvestRepository;
import com.billit.user_service.user.domain.repository.UserInvestRepository;
import com.billit.user_service.user.dto.request.*;
import com.billit.user_service.user.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserInvestService {

    private final UserInvestRepository userInvestRepository;
    private final InvestAccountRepository InvestAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;
    private final EmailVerificationRepository emailVerificationRepository;
    private final MasterCodeRepository masterCodeRepository;

    // 회원가입 - 비밀번호 암호화 추가
    @Transactional
    public UserInvestResponse createUser(UserInvestRequest request) {
        request.validatePassword();

        if (userInvestRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        UserInvest userInvest = UserInvest.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))  // 암호화
                .userName(request.getUserName())
                .phone(request.getPhone())
                .build();

        UserInvest savedUser = userInvestRepository.save(userInvest);
        return UserInvestResponse.of(savedUser);
    }

    // 로그인 - JWT 토큰 발급 추가
    @Transactional(readOnly = false)
    public LoginResponse<UserInvestResponse> login(LoginRequest request) {
        UserInvest user = userInvestRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), "ROLE_InvestER");
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        return LoginResponse.of(accessToken, refreshToken, UserInvestResponse.of(user));
    }

    // 토큰 갱신
    public LoginResponse<UserInvestResponse> refreshToken(String refreshToken) {
        // 리프레시 토큰 검증
        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 새 액세스 토큰 발급
        String userEmail = jwtTokenProvider.getUserEmail(refreshToken);
        UserInvest user = userInvestRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String newAccessToken = jwtTokenProvider.createAccessToken(userEmail, "ROLE_InvestER");

        return LoginResponse.of(newAccessToken, refreshToken, UserInvestResponse.of(user));
    }

    // 비밀번호 검증
    public PasswordVerificationResponse verifyPassword(Long userId, PasswordVerificationRequest request) {
        UserInvest user = userInvestRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        String verificationToken = jwtTokenProvider.createVerificationToken(user.getEmail());
        return PasswordVerificationResponse.builder()
                .verificationToken(verificationToken)
                .expiryTime(LocalDateTime.now().plusMinutes(30))
                .build();
    }

    // 토큰 검증 유틸리티 메서드
    private void validateVerificationToken(String token) {
        if (token == null || !jwtTokenProvider.validateVerificationToken(token)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
    }


    // 마이페이지 조회
    public MyPageResponse getMyPage(Long userId, String verificationToken) {
        validateVerificationToken(verificationToken);
        UserInvest user = userInvestRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return MyPageResponse.of(user);
    }


    // 비밀번호 변경 - 암호화된 비밀번호 비교
    @Transactional
    public void updatePassword(Long userId, PasswordUpdateRequest request, String verificationToken) {
        validateVerificationToken(verificationToken);
        UserInvest user = userInvestRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCHED);
        }

        if (!request.getNewPassword().equals(request.getNewPasswordConfirm())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCHED);
        }

        user.updatePassword(passwordEncoder.encode(request.getNewPassword()));
    }


    // 사용자 정보 조회
    public UserInvestResponse getUserInfo(Long userId) {
        UserInvest user = userInvestRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 해당 사용자의 계좌 정보 조회
        List<AccountInvestResponse> accounts = InvestAccountRepository
                .findAllByUserInvestIdAndIsDeletedFalse(userId)
                .stream()
                .map(AccountInvestResponse::of)
                .collect(Collectors.toList());

        return UserInvestResponse.builder()
                .userInvestId(user.getId())
                .email(user.getEmail())
                .userName(user.getUserName())
                .phone(user.getPhone())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .accounts(accounts)
                .build();
    }

    // 전화번호 변경
    @Transactional
    public void updatePhone(Long userId, PhoneUpdateRequest request, String verificationToken) {
        validateVerificationToken(verificationToken);
        UserInvest user = userInvestRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.updatePhone(request.getPhone());
    }

    @Transactional
    public void logout(String refreshToken) {
        // 리프레시 토큰 검증
        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 리프레시 토큰 폐기 (revoke)
        jwtTokenProvider.revokeRefreshToken(refreshToken);
    }

    @Transactional
    public FindPasswordResponse findPassword(FindPasswordRequest request) {
        UserInvest user = userInvestRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 이름과 전화번호 확인
        if (!user.getUserName().equals(request.getUserName()) ||
                !user.getPhone().equals(request.getPhone())) {
            throw new CustomException(ErrorCode.INVALID_USER_INFO);
        }

        // 임시 비밀번호 생성
        String tempPassword = generateTempPassword();

        // 비밀번호 암호화하여 저장
        user.updatePassword(passwordEncoder.encode(tempPassword));

        // 이메일 발송
        emailService.sendPasswordResetEmail(user.getEmail(), tempPassword);

        // 이메일 마스킹 처리 (예: test@example.com → t***@example.com)
        String maskedEmail = maskEmail(user.getEmail());

        return FindPasswordResponse.builder()
                .userType("InvestER")
                .email(maskedEmail)
                .tempPassword(tempPassword)
                .build();
    }

    // 임시 비밀번호 생성 메서드
    private String generateTempPassword() {
        // 숫자 + 영문자(대,소) + 특수문자 조합으로 10자리
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder tempPassword = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            tempPassword.append(chars.charAt(random.nextInt(chars.length())));
        }

        return tempPassword.toString();
    }

    // 이메일 마스킹 처리 메서드
    private String maskEmail(String email) {
        String[] parts = email.split("@");
        if (parts.length != 2) return email;

        String localPart = parts[0];
        String domain = parts[1];

        if (localPart.length() <= 1) return email;

        return localPart.charAt(0) +
                "*".repeat(localPart.length() - 1) +
                "@" + domain;
    }


    // 회원가입 이메일인증
    @Transactional
    public EmailVerificationResponse sendEmailVerification(EmailVerificationRequest request) {
        // 이미 가입된 이메일인지 확인
        if (userInvestRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        // 인증 코드 생성 (6자리)
        String verificationCode = String.format("%06d", new Random().nextInt(1000000));

        // 인증 정보 저장
        EmailVerification verification = EmailVerification.builder()
                .email(request.getEmail())
                .verificationCode(verificationCode)
                .expiryDate(LocalDateTime.now().plusMinutes(10))
                .build();

        emailVerificationRepository.save(verification);

        // 이메일 발송
        emailService.sendVerificationEmail(request.getEmail(), verificationCode);

        return EmailVerificationResponse.of(
                request.getEmail(),
                "인증 코드가 발송되었습니다. 10분 이내에 인증을 완료해주세요.",
                verification.getExpiryDate()
        );
    }

    // 이메일 인증 코드 확인
    @Transactional
    public EmailVerificationResponse verifyEmail(EmailVerificationConfirmRequest request) {
        // 마스터 코드(0000) 체크 추가
        if ("0000".equals(request.getCode())) {
            EmailVerification verification = EmailVerification.builder()
                    .email(request.getEmail())
                    .verificationCode("0000")
                    .expiryDate(LocalDateTime.now().plusMinutes(10))
                    .build();
            verification.verify();
            emailVerificationRepository.save(verification);

            return EmailVerificationResponse.of(
                    request.getEmail(),
                    "이메일 인증이 완료되었습니다.",
                    null
            );
        }

        EmailVerification verification = emailVerificationRepository
                .findByEmailAndVerificationCode(request.getEmail(), request.getCode())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_VERIFICATION_CODE));

        if (verification.isExpired()) {
            throw new CustomException(ErrorCode.VERIFICATION_CODE_EXPIRED);
        }

        if (verification.isVerified()) {
            throw new CustomException(ErrorCode.ALREADY_VERIFIED);
        }

        verification.verify();
        emailVerificationRepository.save(verification);

        return EmailVerificationResponse.of(
                request.getEmail(),
                "이메일 인증이 완료되었습니다.",
                null
        );
    }
}
