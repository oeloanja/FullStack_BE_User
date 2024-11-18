package com.billit.user_service.user.controller;

import com.billit.user_service.user.dto.request.LoginRequest;
import com.billit.user_service.user.dto.request.UserInvestRequest;
import com.billit.user_service.user.dto.response.UserInvestResponse;
import com.billit.user_service.user.service.UserInvestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/invest")    // '/api' 추가
@RequiredArgsConstructor
public class UserInvestController {

    private final UserInvestService userInvestService;

    @PostMapping("/signup")             // '/signup' 추가
    public ResponseEntity<UserInvestResponse> createUser(
            @Valid @RequestBody UserInvestRequest request) {
        return ResponseEntity.ok(userInvestService.createUser(request));
    }

    @PostMapping("/login")              // 로그인 엔드포인트 추가
    public ResponseEntity<UserInvestResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userInvestService.login(request));
    }
}