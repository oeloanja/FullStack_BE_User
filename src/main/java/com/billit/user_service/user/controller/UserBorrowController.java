package com.billit.user_service.user.controller;

import com.billit.user_service.user.dto.request.*;
import com.billit.user_service.user.dto.response.*;
import com.billit.user_service.user.service.UserBorrowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/borrow")
@RequiredArgsConstructor
public class UserBorrowController {

    private final UserBorrowService userBorrowService;

    @GetMapping
    public ResponseEntity<UserBorrowResponse> getUserInfo(@RequestParam Long userId) {
        return ResponseEntity.ok(userBorrowService.getUserInfo(userId));
    }

    // 비밀번호 검증 엔드포인트 추가
    @PostMapping("/verify-password")
    public ResponseEntity<PasswordVerificationResponse> verifyPassword(
            @RequestParam Long userId,
            @Valid @RequestBody PasswordVerificationRequest request) {
        return ResponseEntity.ok(userBorrowService.verifyPassword(userId, request));
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<UserBorrowResponse> createUser(
            @Valid @RequestBody UserBorrowRequest request) {
        return ResponseEntity.ok(userBorrowService.createUser(request));
    }

    // 인증코드 발송
    @PostMapping("/email/verification")
    public ResponseEntity<EmailVerificationResponse> sendEmailVerification(
            @Valid @RequestBody EmailVerificationRequest request) {
        return ResponseEntity.ok(userBorrowService.sendEmailVerification(request));
    }

    // 인증 확인
    @PostMapping("/email/verify")
    public ResponseEntity<EmailVerificationResponse> verifyEmail(
            @Valid @RequestBody EmailVerificationConfirmRequest request) {
        return ResponseEntity.ok(userBorrowService.verifyEmail(request));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponse<UserBorrowResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userBorrowService.login(request));
    }

    // 마이페이지 조회 - 검증 토큰 필요
    @GetMapping("/mypage")
    public ResponseEntity<MyPageResponse> getMyPage(
            @RequestParam Long userId,
            @RequestHeader(value = "Authorization") String verificationToken) {
        if (verificationToken != null && verificationToken.startsWith("Bearer ")) {
            return ResponseEntity.ok(userBorrowService.getMyPage(userId, verificationToken.substring(7)));
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
            userBorrowService.updatePassword(userId, request, verificationToken.substring(7));
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
            userBorrowService.updatePhone(userId, request, verificationToken.substring(7));
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // 토큰 갱신
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse<UserBorrowResponse>> refreshToken(
            @RequestHeader("Refresh-Token") String refreshToken) {
        return ResponseEntity.ok(userBorrowService.refreshToken(refreshToken));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Refresh-Token") String refreshToken) {
        userBorrowService.logout(refreshToken);
        return ResponseEntity.ok().build();
    }

    // 비밀번호 찾기
    @PostMapping("/find-password")
    public ResponseEntity<FindPasswordResponse> findPassword(
            @Valid @RequestBody FindPasswordRequest request) {
        return ResponseEntity.ok(userBorrowService.findPassword(request));
    }
}