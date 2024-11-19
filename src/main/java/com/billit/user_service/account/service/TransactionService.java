package com.billit.user_service.account.service;

import com.billit.user_service.account.domain.entity.BorrowAccount;
import com.billit.user_service.account.domain.entity.Transaction;
import com.billit.user_service.account.domain.entity.enums.TransactionType;
import com.billit.user_service.account.domain.repository.BorrowAccountRepository;
import com.billit.user_service.account.domain.repository.InvestAccountRepository;
import com.billit.user_service.account.domain.repository.TransactionRepository;
import com.billit.user_service.account.dto.request.DepositRequest;
import com.billit.user_service.account.dto.request.TransferRequest;
import com.billit.user_service.account.dto.request.WithdrawRequest;
import com.billit.user_service.account.dto.response.TransactionResponse;
import com.billit.user_service.common.exception.CustomException;
import com.billit.user_service.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final BorrowAccountRepository borrowAccountRepository;
    private final InvestAccountRepository investAccountRepository;

    @Transactional
    public TransactionResponse deposit(Long userId, DepositRequest request) {
        BorrowAccount account = borrowAccountRepository
                .findByAccountNumberAndIsDeletedFalse(request.getAccountNumber())
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        validateAccountOwnership(account, userId);

        Transaction transaction = Transaction.builder()
                .transactionId(generateTransactionId())
                .borrowAccount(account)
                .amount(request.getAmount())
                .type(TransactionType.DEPOSIT)
                .description(request.getDescription())
                .build();

        try {
            account.deposit(request.getAmount());
            transaction.complete();
        } catch (Exception e) {
            transaction.fail();
            throw new CustomException(ErrorCode.TRANSACTION_FAILED);
        }

        return TransactionResponse.of(transactionRepository.save(transaction));
    }

    @Transactional
    public TransactionResponse withdraw(Long userId, WithdrawRequest request) {
        BorrowAccount account = borrowAccountRepository
                .findByAccountNumberAndIsDeletedFalse(request.getAccountNumber())
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        validateAccountOwnership(account, userId);

        Transaction transaction = Transaction.builder()
                .transactionId(generateTransactionId())
                .borrowAccount(account)
                .amount(request.getAmount())
                .type(TransactionType.WITHDRAW)
                .description(request.getDescription())
                .build();

        try {
            account.withdraw(request.getAmount());
            transaction.complete();
        } catch (CustomException e) {
            transaction.fail();
            throw e;
        }

        return TransactionResponse.of(transactionRepository.save(transaction));
    }

    @Transactional
    public TransactionResponse transfer(Long userId, TransferRequest request) {
        BorrowAccount fromAccount = borrowAccountRepository
                .findByAccountNumberAndIsDeletedFalse(request.getFromAccountNumber())
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        validateAccountOwnership(fromAccount, userId);

        BorrowAccount toAccount = borrowAccountRepository
                .findByAccountNumberAndIsDeletedFalse(request.getToAccountNumber())
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        Transaction transaction = Transaction.builder()
                .transactionId(generateTransactionId())
                .borrowAccount(fromAccount)
                .amount(request.getAmount())
                .type(TransactionType.TRANSFER)
                .description(request.getDescription())
                .build();

        try {
            fromAccount.withdraw(request.getAmount());
            toAccount.deposit(request.getAmount());
            transaction.complete();
        } catch (CustomException e) {
            transaction.fail();
            throw e;
        }

        return TransactionResponse.of(transactionRepository.save(transaction));
    }

    public List<TransactionResponse> getTransactionHistory(Long userId, Long accountId) {
        BorrowAccount account = borrowAccountRepository
                .findByIdAndIsDeletedFalse(accountId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        validateAccountOwnership(account, userId);

        return transactionRepository.findByBorrowAccountId(accountId)
                .stream()
                .map(TransactionResponse::of)
                .collect(Collectors.toList());
    }

    private String generateTransactionId() {
        return UUID.randomUUID().toString();
    }

    private void validateAccountOwnership(BorrowAccount account, Long userId) {
        if (!account.getUserBorrow().getId().equals(userId)) {
            throw new CustomException(ErrorCode.ACCOUNT_USER_MISMATCH);
        }
    }
    public BigDecimal getBalance(Long userId, Long accountId) {
        BorrowAccount account = borrowAccountRepository
                .findByIdAndIsDeletedFalse(accountId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        validateAccountOwnership(account, userId);

        return account.getBalance();
    }
}