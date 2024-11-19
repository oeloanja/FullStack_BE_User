package com.billit.user_service.user.controller;

import com.billit.user_service.user.dto.request.LoginRequest;
import com.billit.user_service.user.dto.request.PasswordUpdateRequest;
import com.billit.user_service.user.dto.request.PhoneUpdateRequest;
import com.billit.user_service.user.dto.request.UserInvestRequest;
import com.billit.user_service.user.dto.response.MyPageResponse;
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

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<UserInvestResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userInvestService.login(request));
    }

    // 마이페이지 조회
    @GetMapping("/mypage")
    public ResponseEntity<MyPageResponse> getMyPage(@RequestParam Long userId) {
        return ResponseEntity.ok(userInvestService.getMyPage(userId));
    }

    // 비밀번호 변경
    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(
            @RequestParam Long userId,
            @Valid @RequestBody PasswordUpdateRequest request) {
        userInvestService.updatePassword(userId, request);
        return ResponseEntity.ok().build();
    }

    // 전화번호 변경
    @PutMapping("/phone")
    public ResponseEntity<Void> updatePhone(
            @RequestParam Long userId,
            @Valid @RequestBody PhoneUpdateRequest request) {
        userInvestService.updatePhone(userId, request);
        return ResponseEntity.ok().build();
    }
}