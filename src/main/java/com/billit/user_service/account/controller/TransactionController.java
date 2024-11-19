package com.billit.user_service.account.controller;

import com.billit.user_service.account.dto.request.DepositRequest;
import com.billit.user_service.account.dto.request.TransferRequest;
import com.billit.user_service.account.dto.request.WithdrawRequest;
import com.billit.user_service.account.dto.response.TransactionResponse;
import com.billit.user_service.account.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/accounts/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    // 잔액 조회
    @GetMapping("/balance/{accountId}")
    public ResponseEntity<BigDecimal> getBalance(
            @RequestParam Long userId,
            @PathVariable Long accountId) {
        return ResponseEntity.ok(transactionService.getBalance(userId, accountId));
    }

    // 입금
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(
            @RequestParam Long userId,
            @Valid @RequestBody DepositRequest request) {
        return ResponseEntity.ok(transactionService.deposit(userId, request));
    }

    // 출금
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(
            @RequestParam Long userId,
            @Valid @RequestBody WithdrawRequest request) {
        return ResponseEntity.ok(transactionService.withdraw(userId, request));
    }

    // 송금
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(
            @RequestParam Long userId,
            @Valid @RequestBody TransferRequest request) {
        return ResponseEntity.ok(transactionService.transfer(userId, request));
    }

    // 거래 내역 조회
    @GetMapping("/history/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionHistory(
            @RequestParam Long userId,
            @PathVariable Long accountId) {
        return ResponseEntity.ok(transactionService.getTransactionHistory(userId, accountId));
    }
}