package com.billit.user_service.user.controller;

import com.billit.user_service.user.dto.request.LoginRequest;
import com.billit.user_service.user.dto.request.UserBorrowRequest;
import com.billit.user_service.user.dto.response.UserBorrowResponse;
import com.billit.user_service.user.service.UserBorrowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/borrow")    // '/api' 추가
@RequiredArgsConstructor
public class UserBorrowController {

    private final UserBorrowService userborrowService;

    @PostMapping("/signup")             // '/signup' 추가
    public ResponseEntity<UserBorrowResponse> createUser(
            @Valid @RequestBody UserBorrowRequest request) {
        return ResponseEntity.ok(userborrowService.createUser(request));
    }

    @PostMapping("/login")              // 로그인 엔드포인트 추가
    public ResponseEntity<UserBorrowResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userborrowService.login(request));
    }
}