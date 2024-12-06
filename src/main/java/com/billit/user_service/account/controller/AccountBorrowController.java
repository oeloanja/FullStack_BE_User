package com.billit.user_service.account.controller;

import com.billit.user_service.account.dto.request.AccountBorrowRequest;
import com.billit.user_service.account.dto.request.AccountStatusRequest;
import com.billit.user_service.account.dto.response.AccountBorrowResponse;
import com.billit.user_service.account.service.AccountBorrowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts/borrow")
@RequiredArgsConstructor
public class AccountBorrowController {

    private final AccountBorrowService accountBorrowService;

    // 계좌 등록
    @PostMapping
    public ResponseEntity<AccountBorrowResponse> createAccount(
            @RequestParam UUID userId,
            @Valid @RequestBody AccountBorrowRequest request) {
        return ResponseEntity.ok(accountBorrowService.createAccount(userId, request));
    }

    // 계좌 목록 조회
    @GetMapping
    public ResponseEntity<List<AccountBorrowResponse>> getAccounts(
            @RequestParam UUID userId) {
        return ResponseEntity.ok(accountBorrowService.getAccounts(userId));
    }

    // 계좌 삭제
    @PutMapping("/{accountId}/status")
    public ResponseEntity<Void> updateAccountStatus(
            @RequestParam UUID userId,
            @PathVariable Integer accountId,
            @RequestBody AccountStatusRequest request) {    // request 매개변수 추가
        accountBorrowService.deleteAccount(userId, accountId);
        return ResponseEntity.ok().build();
    }
}