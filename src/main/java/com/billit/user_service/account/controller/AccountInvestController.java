package com.billit.user_service.account.controller;

import com.billit.user_service.account.dto.request.AccountInvestRequest;
import com.billit.user_service.account.dto.request.AccountStatusRequest;
import com.billit.user_service.account.dto.response.AccountInvestResponse;
import com.billit.user_service.account.service.AccountInvestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts/invest")
@RequiredArgsConstructor
public class AccountInvestController {

    private final AccountInvestService accountInvestService;

    // 계좌 등록
    @PostMapping
    public ResponseEntity<AccountInvestResponse> createAccount(
            @RequestParam Long userId,
            @Valid @RequestBody AccountInvestRequest request) {
        return ResponseEntity.ok(accountInvestService.createAccount(userId, request));
    }

    // 계좌 목록 조회
    @GetMapping
    public ResponseEntity<List<AccountInvestResponse>> getAccounts(
            @RequestParam Long userId) {
        return ResponseEntity.ok(accountInvestService.getAccounts(userId));
    }

    // 계좌 삭제
    @PutMapping("/{accountId}/status")
    public ResponseEntity<Void> updateAccountStatus(
            @RequestParam Long userId,
            @PathVariable Long accountId,
            @RequestBody AccountStatusRequest request) {    // request 매개변수 추가
        accountInvestService.deleteAccount(userId, accountId);
        return ResponseEntity.ok().build();
    }
}