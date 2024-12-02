package com.billit.user_service.user.controller;

import com.billit.user_service.user.dto.request.*;
import com.billit.user_service.user.dto.response.*;
import com.billit.user_service.user.service.UserInvestService;
import com.billit.user_service.user.service.UserInvestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/invest")
@RequiredArgsConstructor
public class UserInvestController {

    private final UserInvestService userInvestService;

    @GetMapping
    public ResponseEntity<UserInvestResponse> getUserInfo(@RequestParam Long userId) {
        return ResponseEntity.ok(userInvestService.getUserInfo(userId));
    }

    // 비밀번호 검증 엔드포인트 추가
    @PostMapping("/verify-password")
    public ResponseEntity<PasswordVerificationResponse> verifyPassword(
            @RequestParam Long userId,
            @Valid @RequestBody PasswordVerificationRequest request) {
        return ResponseEntity.ok(userInvestService.verifyPassword(userId, request));
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<UserInvestResponse> createUser(
            @Valid @RequestBody UserInvestRequest request) {
        return ResponseEntity.ok(userInvestService.createUser(request));
    }

    // 인증코드 발송
    @PostMapping("/email/verification")
    public ResponseEntity<EmailVerificationResponse> sendEmailVerification(
            @Valid @RequestBody EmailVerificationRequest request) {
        return ResponseEntity.ok(userInvestService.sendEmailVerification(request));
    }

    // 인증 확인
    @PostMapping("/email/verify")
    public ResponseEntity<EmailVerificationResponse> verifyEmail(
            @Valid @RequestBody EmailVerificationConfirmRequest request) {
        return ResponseEntity.ok(userInvestService.verifyEmail(request));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponse<UserInvestResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userInvestService.login(request));
    }

    // 마이페이지 조회 - 검증 토큰 필요
    @GetMapping("/mypage")
    public ResponseEntity<MyPageResponse> getMyPage(
            @RequestParam Long userId,
            @RequestHeader(value = "Authorization") String verificationToken) {
        if (verificationToken != null && verificationToken.startsWith("Bearer ")) {
            return ResponseEntity.ok(userInvestService.getMyPage(userId, verificationToken.substring(7)));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // 비밀번호 변경 - 검증 토큰 필요
    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(
            @RequestParam Long userId,
            @RequestHeader(value = "Authorization") String verificationToken,
            @Valid @RequestBody PasswordUpdateRequest request) {
        if (verificationToken != null && verificationToken.startsWith("Bearer ")) {
            userInvestService.updatePassword(userId, request, verificationToken.substring(7));
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // 전화번호 변경 - 검증 토큰 필요
    @PutMapping("/phone")
    public ResponseEntity<Void> updatePhone(
            @RequestParam Long userId,
            @RequestHeader(value = "Authorization") String verificationToken,
            @Valid @RequestBody PhoneUpdateRequest request) {
        if (verificationToken != null && verificationToken.startsWith("Bearer ")) {
            userInvestService.updatePhone(userId, request, verificationToken.substring(7));
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // 토큰 갱신
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse<UserInvestResponse>> refreshToken(
            @RequestHeader("Refresh-Token") String refreshToken) {
        return ResponseEntity.ok(userInvestService.refreshToken(refreshToken));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Refresh-Token") String refreshToken) {
        userInvestService.logout(refreshToken);
        return ResponseEntity.ok().build();
    }

    // 비밀번호 찾기
    @PostMapping("/find-password")
    public ResponseEntity<FindPasswordResponse> findPassword(
            @Valid @RequestBody FindPasswordRequest request) {
        return ResponseEntity.ok(userInvestService.findPassword(request));
    }
}