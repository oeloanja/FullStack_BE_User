package com.billit.user_service.user.controller;

import com.billit.user_service.user.dto.request.LoginRequest;
import com.billit.user_service.user.dto.request.PasswordUpdateRequest;
import com.billit.user_service.user.dto.request.PhoneUpdateRequest;
import com.billit.user_service.user.dto.request.UserInvestRequest;
import com.billit.user_service.user.dto.response.LoginResponse;
import com.billit.user_service.user.dto.response.MyPageResponse;
import com.billit.user_service.user.dto.response.UserBorrowResponse;
import com.billit.user_service.user.dto.response.UserInvestResponse;
import com.billit.user_service.user.service.UserInvestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users/invest")
@RequiredArgsConstructor
public class UserInvestController {

    private final UserInvestService userInvestService;

    @GetMapping
    public ResponseEntity<UserInvestResponse> getUserInfo(@RequestParam Long userId) {
        return ResponseEntity.ok(userInvestService.getUserInfo(userId));
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<UserInvestResponse> createUser(
            @Valid @RequestBody UserInvestRequest request) {
        return ResponseEntity.ok(userInvestService.createUser(request));
    }

    // 로그인 - JWT 토큰 응답 추가
    @PostMapping("/login")
    public ResponseEntity<LoginResponse<UserInvestResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userInvestService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse<UserInvestResponse>> refresh(
            @RequestHeader("Authorization") String refreshToken) {
        // Bearer 제거
        String token = refreshToken.substring(7);
        return ResponseEntity.ok(userInvestService.refreshToken(token));
    }


    // 마이페이지 조회 - JWT 토큰으로 인증
    @GetMapping("/mypage")
    public ResponseEntity<MyPageResponse> getMyPage(@RequestParam Long userId) {
        return ResponseEntity.ok(userInvestService.getMyPage(userId));
    }

    // 비밀번호 변경 - JWT 토큰으로 인증
    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(
            @RequestParam Long userId,
            @Valid @RequestBody PasswordUpdateRequest request) {
        userInvestService.updatePassword(userId, request);
        return ResponseEntity.ok().build();
    }

    // 전화번호 변경 - JWT 토큰으로 인증
    @PutMapping("/phone")
    public ResponseEntity<Void> updatePhone(
            @RequestParam Long userId,
            @Valid @RequestBody PhoneUpdateRequest request) {
        userInvestService.updatePhone(userId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String refreshToken) {
        // Bearer 제거
        String token = refreshToken.substring(7);
        userInvestService.logout(token);  // borrow 또는 invest service
        return ResponseEntity.ok().build();
    }
}