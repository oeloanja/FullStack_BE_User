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
import java.util.UUID;

@RestController
@RequestMapping("/accounts/borrow/transaction")
@RequiredArgsConstructor
public class BorrowTransactionController {
    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(
            @RequestParam UUID userId,
            @Valid @RequestBody DepositRequest request) {
        return ResponseEntity.ok(transactionService.depositBorrow(userId, request));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(
            @RequestParam UUID userId,
            @Valid @RequestBody WithdrawRequest request) {
        return ResponseEntity.ok(transactionService.withdrawBorrow(userId, request));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(
            @RequestParam UUID userId,
            @Valid @RequestBody TransferRequest request) {
        return ResponseEntity.ok(transactionService.transferBorrow(userId, request));
    }

    @GetMapping("/history/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionHistory(
            @RequestParam UUID userId,
            @PathVariable Integer accountId) {
        return ResponseEntity.ok(transactionService.getBorrowTransactionHistory(userId, accountId));
    }

    @GetMapping("/balance/{accountId}")
    public ResponseEntity<BigDecimal> getBalance(
            @RequestParam UUID userId,
            @PathVariable Integer accountId) {
        return ResponseEntity.ok(transactionService.getBorrowBalance(userId, accountId));
    }
}