package com.billit.user_service.user.controller;

import com.billit.user_service.user.dto.request.LoginRequest;
import com.billit.user_service.user.dto.request.PasswordUpdateRequest;
import com.billit.user_service.user.dto.request.PhoneUpdateRequest;
import com.billit.user_service.user.dto.request.UserBorrowRequest;
import com.billit.user_service.user.dto.response.LoginResponse;
import com.billit.user_service.user.dto.response.MyPageResponse;
import com.billit.user_service.user.dto.response.UserBorrowResponse;
import com.billit.user_service.user.service.UserBorrowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users/borrow")
@RequiredArgsConstructor
public class UserBorrowController {

    private final UserBorrowService userBorrowService;

    @GetMapping
    public ResponseEntity<UserBorrowResponse> getUserInfo(@RequestParam Long userId) {
        return ResponseEntity.ok(userBorrowService.getUserInfo(userId));
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<UserBorrowResponse> createUser(
            @Valid @RequestBody UserBorrowRequest request) {
        return ResponseEntity.ok(userBorrowService.createUser(request));
    }

    // 로그인 - JWT 토큰 응답 추가
    @PostMapping("/login")
    public ResponseEntity<LoginResponse<UserBorrowResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userBorrowService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse<UserBorrowResponse>> refresh(
            @RequestHeader("Authorization") String refreshToken) {
        // Bearer 제거
        String token = refreshToken.substring(7);
        return ResponseEntity.ok(userBorrowService.refreshToken(token));
    }


    // 마이페이지 조회 - JWT 토큰으로 인증
    @GetMapping("/mypage")
    public ResponseEntity<MyPageResponse> getMyPage(@RequestParam Long userId) {
        return ResponseEntity.ok(userBorrowService.getMyPage(userId));
    }

    // 비밀번호 변경 - JWT 토큰으로 인증
    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(
            @RequestParam Long userId,
            @Valid @RequestBody PasswordUpdateRequest request) {
        userBorrowService.updatePassword(userId, request);
        return ResponseEntity.ok().build();
    }

    // 전화번호 변경 - JWT 토큰으로 인증
    @PutMapping("/phone")
    public ResponseEntity<Void> updatePhone(
            @RequestParam Long userId,
            @Valid @RequestBody PhoneUpdateRequest request) {
        userBorrowService.updatePhone(userId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String refreshToken) {
        // Bearer 제거
        String token = refreshToken.substring(7);
        userBorrowService.logout(token);  // borrow 또는 invest service
        return ResponseEntity.ok().build();
    }
}