package com.billit.user_service.account.service;

import com.billit.user_service.account.domain.entity.BorrowAccount;
import com.billit.user_service.account.domain.entity.InvestAccount;
import com.billit.user_service.account.domain.entity.Transaction;
import com.billit.user_service.account.domain.entity.enums.TransactionType;
import com.billit.user_service.account.domain.repository.BorrowAccountRepository;
import com.billit.user_service.account.domain.repository.InvestAccountRepository;
import com.billit.user_service.account.domain.repository.TransactionRepository;
import com.billit.user_service.account.dto.request.DepositRequest;
import com.billit.user_service.account.dto.request.GroupDepositRequest;
import com.billit.user_service.account.dto.request.TransferRequest;
import com.billit.user_service.account.dto.request.WithdrawRequest;
import com.billit.user_service.account.dto.response.TransactionResponse;
import com.billit.user_service.common.exception.CustomException;
import com.billit.user_service.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final BorrowAccountRepository borrowAccountRepository;
    private final InvestAccountRepository investAccountRepository;

    // 대출자 계좌 잔액 조회
    public BigDecimal getBorrowBalance(Long userId, Integer accountId) {
        BorrowAccount account = borrowAccountRepository
                .findByIdAndIsDeletedFalse(accountId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
        validateBorrowAccountOwnership(account, userId);
        return account.getBalance();
    }

    // 투자자 계좌 잔액 조회
    public BigDecimal getInvestBalance(Long userId, Integer accountId) {
        InvestAccount account = investAccountRepository
                .findByIdAndIsDeletedFalse(accountId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
        validateInvestAccountOwnership(account, userId);
        return account.getBalance();
    }

    // 대출자 계좌 입금
    @Transactional
    public TransactionResponse depositBorrow(Long userId, DepositRequest request) {
        BorrowAccount account = borrowAccountRepository
                .findByIdAndIsDeletedFalse(request.getAccountId())
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
        validateBorrowAccountOwnership(account, userId);

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

    // 그룹단위 입금
    @Transactional
    public List<TransactionResponse> depositGroupBorrow(List<GroupDepositRequest> requests) {
        requests.forEach(request -> {
            log.info("Processing GroupDepositRequest: {}", request);
            if (request.getUserBorrowAccountId() == null) {
                log.error("accountBorrowId is null in request: {}", request);
            }
        });

        return requests.stream()
                .map(request -> {
                    // 1. 각 대출자의 계좌 조회
                    BorrowAccount account = borrowAccountRepository.findById(Long.valueOf(request.getUserBorrowAccountId()))
                            .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

                    // 2. 잔액 업데이트
                    account.deposit(request.getAmount());
                    borrowAccountRepository.save(account);

                    // 3. 거래 내역 생성
                    Transaction transaction = Transaction.builder()
                            .transactionId(generateTransactionId())
                            .borrowAccount(account)
                            .amount(request.getAmount())
                            .type(TransactionType.DEPOSIT)
                            .description(request.getDescription())
                            .build();

                    // 4. 거래 내역 저장
                    Transaction savedTransaction = transactionRepository.save(transaction);

                    // 5. 응답 생성
                    return TransactionResponse.of(savedTransaction);
                })
                .collect(Collectors.toList());
    }

    // 투자자 계좌 입금
    @Transactional
    public TransactionResponse depositInvest(Long userId, DepositRequest request) {
        InvestAccount account = investAccountRepository
                .findByIdAndIsDeletedFalse(request.getAccountId())
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
        validateInvestAccountOwnership(account, userId);

        Transaction transaction = Transaction.builder()
                .transactionId(generateTransactionId())
                .investAccount(account)
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

    // 대출자 계좌 출금
    @Transactional
    public TransactionResponse withdrawBorrow(Long userId, WithdrawRequest request) {
        BorrowAccount account = borrowAccountRepository
                .findByAccountNumberAndIsDeletedFalse(request.getAccountNumber())
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
        validateBorrowAccountOwnership(account, userId);

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
        } catch (Exception e) {
            transaction.fail();
            throw new CustomException(ErrorCode.TRANSACTION_FAILED);
        }

        return TransactionResponse.of(transactionRepository.save(transaction));
    }

    // 투자자 계좌 출금
    @Transactional
    public TransactionResponse withdrawInvest(Long userId, WithdrawRequest request) {
        InvestAccount account = investAccountRepository
                .findByAccountNumberAndIsDeletedFalse(request.getAccountNumber())
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
        validateInvestAccountOwnership(account, userId);

        Transaction transaction = Transaction.builder()
                .transactionId(generateTransactionId())
                .investAccount(account)
                .amount(request.getAmount())
                .type(TransactionType.WITHDRAW)
                .description(request.getDescription())
                .build();

        try {
            account.withdraw(request.getAmount());
            transaction.complete();
        } catch (Exception e) {
            transaction.fail();
            throw new CustomException(ErrorCode.TRANSACTION_FAILED);
        }

        return TransactionResponse.of(transactionRepository.save(transaction));
    }

    // 대출자 계좌 송금
    @Transactional
    public TransactionResponse transferBorrow(Long userId, TransferRequest request) {
        BorrowAccount fromAccount = borrowAccountRepository
                .findByAccountNumberAndIsDeletedFalse(request.getFromAccountNumber())
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
        validateBorrowAccountOwnership(fromAccount, userId);

        // 받는 계좌 확인 (대출자/투자자)
        BorrowAccount toBorrowAccount = borrowAccountRepository
                .findByAccountNumberAndIsDeletedFalse(request.getToAccountNumber())
                .orElse(null);

        InvestAccount toInvestAccount = null;
        if (toBorrowAccount == null) {
            toInvestAccount = investAccountRepository
                    .findByAccountNumberAndIsDeletedFalse(request.getToAccountNumber())
                    .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
        }

        // 송금하는 쪽(대출자)의 거래 내역
        Transaction sendTransaction = Transaction.builder()
                .transactionId(generateTransactionId())
                .borrowAccount(fromAccount)
                .amount(request.getAmount())
                .type(TransactionType.TRANSFER)
                .description(request.getDescription() + " (출금)")
                .build();

        // 받는 쪽의 거래 내역
        Transaction receiveTransaction = Transaction.builder()
                .transactionId(generateTransactionId())
                .amount(request.getAmount())
                .type(TransactionType.TRANSFER)
                .description(request.getDescription() + " (입금)")
                .build();

        try {
            fromAccount.withdraw(request.getAmount());
            if (toBorrowAccount != null) {
                toBorrowAccount.deposit(request.getAmount());
                receiveTransaction.setBorrowAccount(toBorrowAccount);
            } else {
                toInvestAccount.deposit(request.getAmount());
                receiveTransaction.setInvestAccount(toInvestAccount);
            }

            sendTransaction.complete();
            receiveTransaction.complete();

            transactionRepository.save(sendTransaction);
            transactionRepository.save(receiveTransaction);

        } catch (Exception e) {
            sendTransaction.fail();
            receiveTransaction.fail();
            throw new CustomException(ErrorCode.TRANSACTION_FAILED);
        }

        return TransactionResponse.of(sendTransaction);
    }

    // 투자자 계좌 송금
    @Transactional
    public TransactionResponse transferInvest(Long userId, TransferRequest request) {
        InvestAccount fromAccount = investAccountRepository
                .findByAccountNumberAndIsDeletedFalse(request.getFromAccountNumber())
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
        validateInvestAccountOwnership(fromAccount, userId);

        // 받는 계좌 확인 (대출자/투자자)
        InvestAccount toInvestAccount = investAccountRepository
                .findByAccountNumberAndIsDeletedFalse(request.getToAccountNumber())
                .orElse(null);

        BorrowAccount toBorrowAccount = null;
        if (toInvestAccount == null) {
            toBorrowAccount = borrowAccountRepository
                    .findByAccountNumberAndIsDeletedFalse(request.getToAccountNumber())
                    .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
        }

        // 송금하는 쪽(투자자)의 거래 내역
        Transaction sendTransaction = Transaction.builder()
                .transactionId(generateTransactionId())
                .investAccount(fromAccount)
                .amount(request.getAmount())
                .type(TransactionType.TRANSFER)
                .description(request.getDescription() + " (출금)")
                .build();

        // 받는 쪽의 거래 내역
        Transaction receiveTransaction = Transaction.builder()
                .transactionId(generateTransactionId())
                .amount(request.getAmount())
                .type(TransactionType.TRANSFER)
                .description(request.getDescription() + " (입금)")
                .build();

        try {
            fromAccount.withdraw(request.getAmount());
            if (toInvestAccount != null) {
                toInvestAccount.deposit(request.getAmount());
                receiveTransaction.setInvestAccount(toInvestAccount);
            } else {
                toBorrowAccount.deposit(request.getAmount());
                receiveTransaction.setBorrowAccount(toBorrowAccount);
            }

            sendTransaction.complete();
            receiveTransaction.complete();

            transactionRepository.save(sendTransaction);
            transactionRepository.save(receiveTransaction);

        } catch (Exception e) {
            sendTransaction.fail();
            receiveTransaction.fail();
            throw new CustomException(ErrorCode.TRANSACTION_FAILED);
        }

        return TransactionResponse.of(sendTransaction);
    }

    // 대출자 계좌 거래 내역 조회
    public List<TransactionResponse> getBorrowTransactionHistory(Long userId, Integer accountId) {
        BorrowAccount account = borrowAccountRepository
                .findByIdAndIsDeletedFalse(accountId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
        validateBorrowAccountOwnership(account, userId);

        return transactionRepository.findByBorrowAccountId(accountId)
                .stream()
                .map(TransactionResponse::of)
                .collect(Collectors.toList());
    }

    // 투자자 계좌 거래 내역 조회
    public List<TransactionResponse> getInvestTransactionHistory(Long userId, Integer accountId) {
        InvestAccount account = investAccountRepository
                .findByIdAndIsDeletedFalse(accountId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
        validateInvestAccountOwnership(account, userId);

        return transactionRepository.findByInvestAccountId(accountId)
                .stream()
                .map(TransactionResponse::of)
                .collect(Collectors.toList());
    }

    private String generateTransactionId() {
        return UUID.randomUUID().toString();
    }

    private void validateBorrowAccountOwnership(BorrowAccount account, Long userId) {
        if (!account.getUserBorrow().getId().equals(userId)) {
            throw new CustomException(ErrorCode.ACCOUNT_USER_MISMATCH);
        }
    }

    private void validateInvestAccountOwnership(InvestAccount account, Long userId) {
        if (!account.getUserInvest().getId().equals(userId)) {
            throw new CustomException(ErrorCode.ACCOUNT_USER_MISMATCH);
        }
    }
}