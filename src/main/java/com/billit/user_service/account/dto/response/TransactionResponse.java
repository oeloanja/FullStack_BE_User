package com.billit.user_service.account.dto.response;

import com.billit.user_service.account.domain.entity.Transaction;
import com.billit.user_service.account.domain.entity.enums.TransactionStatus;
import com.billit.user_service.account.domain.entity.enums.TransactionType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class TransactionResponse {
    private String transactionId;
    private String accountNumber;
    private BigDecimal amount;
    private TransactionType type;
    private TransactionStatus status;
    private String description;
    private LocalDateTime transactionDate;

    public static TransactionResponse of(Transaction transaction) {
        String accountNumber = transaction.getBorrowAccount() != null ?
                transaction.getBorrowAccount().getAccountNumber() :
                transaction.getInvestAccount().getAccountNumber();

        return TransactionResponse.builder()
                .transactionId(transaction.getTransactionId())
                .accountNumber(accountNumber)
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .status(transaction.getStatus())
                .description(transaction.getDescription())
                .transactionDate(transaction.getCreatedAt())
                .build();
    }
}