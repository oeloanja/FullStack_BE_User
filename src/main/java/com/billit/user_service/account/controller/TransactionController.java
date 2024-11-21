package com.billit.user_service.account.controller;

import com.billit.user_service.account.dto.request.DepositRequest;
import com.billit.user_service.account.dto.request.GroupDepositRequest;
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

    // 대출자 계좌 잔액 조회
    @GetMapping("/borrow/balance/{accountId}")
    public ResponseEntity<BigDecimal> getBorrowBalance(
            @RequestParam Long userId,
            @PathVariable Integer accountId) {
        return ResponseEntity.ok(transactionService.getBorrowBalance(userId, accountId));
    }

    // 투자자 계좌 잔액 조회
    @GetMapping("/invest/balance/{accountId}")
    public ResponseEntity<BigDecimal> getInvestBalance(
            @RequestParam Long userId,
            @PathVariable Integer accountId) {
        return ResponseEntity.ok(transactionService.getInvestBalance(userId, accountId));
    }

    // 대출자 계좌 입금
    @PostMapping("/borrow/deposit")
    public ResponseEntity<TransactionResponse> depositBorrow(
            @RequestParam Long userId,
            @Valid @RequestBody DepositRequest request) {
        return ResponseEntity.ok(transactionService.depositBorrow(userId, request));
    }

    // 그룹 단위의 입금
    @PostMapping("/group/borrow/deposit")
    public ResponseEntity<List<TransactionResponse>> depositGroupBorrow(
            @Valid @RequestBody List<GroupDepositRequest> requests) {
        return ResponseEntity.ok(transactionService.depositGroupBorrow(requests));
    }

    // 투자자 계좌 입금
    @PostMapping("/invest/deposit")
    public ResponseEntity<TransactionResponse> depositInvest(
            @RequestParam Long userId,
            @Valid @RequestBody DepositRequest request) {
        return ResponseEntity.ok(transactionService.depositInvest(userId, request));
    }

    // 대출자 계좌 출금
    @PostMapping("/borrow/withdraw")
    public ResponseEntity<TransactionResponse> withdrawBorrow(
            @RequestParam Long userId,
            @Valid @RequestBody WithdrawRequest request) {
        return ResponseEntity.ok(transactionService.withdrawBorrow(userId, request));
    }

    // 투자자 계좌 출금
    @PostMapping("/invest/withdraw")
    public ResponseEntity<TransactionResponse> withdrawInvest(
            @RequestParam Long userId,
            @Valid @RequestBody WithdrawRequest request) {
        return ResponseEntity.ok(transactionService.withdrawInvest(userId, request));
    }

    // 대출자 계좌 송금
    @PostMapping("/borrow/transfer")
    public ResponseEntity<TransactionResponse> transferBorrow(
            @RequestParam Long userId,
            @Valid @RequestBody TransferRequest request) {
        return ResponseEntity.ok(transactionService.transferBorrow(userId, request));
    }

    // 투자자 계좌 송금
    @PostMapping("/invest/transfer")
    public ResponseEntity<TransactionResponse> transferInvest(
            @RequestParam Long userId,
            @Valid @RequestBody TransferRequest request) {
        return ResponseEntity.ok(transactionService.transferInvest(userId, request));
    }

    // 대출자 계좌 거래 내역 조회
    @GetMapping("/borrow/history/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getBorrowTransactionHistory(
            @RequestParam Long userId,
            @PathVariable Integer accountId) {
        return ResponseEntity.ok(transactionService.getBorrowTransactionHistory(userId, accountId));
    }

    // 투자자 계좌 거래 내역 조회
    @GetMapping("/invest/history/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getInvestTransactionHistory(
            @RequestParam Long userId,
            @PathVariable Integer accountId) {
        return ResponseEntity.ok(transactionService.getInvestTransactionHistory(userId, accountId));
    }
}