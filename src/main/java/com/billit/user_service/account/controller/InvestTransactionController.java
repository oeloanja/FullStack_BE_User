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
@RequestMapping("/accounts/invest/transaction")
@RequiredArgsConstructor
public class InvestTransactionController {
    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(
            @RequestParam UUID userId,
            @Valid @RequestBody DepositRequest request) {
        return ResponseEntity.ok(transactionService.depositInvest(userId, request));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(
            @RequestParam UUID userId,
            @Valid @RequestBody WithdrawRequest request) {
        return ResponseEntity.ok(transactionService.withdrawInvest(userId, request));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(
            @RequestParam UUID userId,
            @Valid @RequestBody TransferRequest request) {
        return ResponseEntity.ok(transactionService.transferInvest(userId, request));
    }

    @GetMapping("/history/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionHistory(
            @RequestParam UUID userId,
            @PathVariable Integer accountId) {
        return ResponseEntity.ok(transactionService.getInvestTransactionHistory(userId, accountId));
    }

    @GetMapping("/balance/{accountId}")
    public ResponseEntity<BigDecimal> getBalance(
            @RequestParam UUID userId,
            @PathVariable Integer accountId) {
        return ResponseEntity.ok(transactionService.getInvestBalance(userId, accountId));
    }
}