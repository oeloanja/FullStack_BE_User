package com.billit.user_service.account.dto.response;

import com.billit.user_service.account.domain.entity.BorrowAccount;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class AccountBorrowResponse {
    private Integer id;
    private String bankName;
    private String accountNumber;
    private String accountHolder;
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AccountBorrowResponse of(BorrowAccount account) {
        return AccountBorrowResponse.builder()
                .id(account.getId())
                .bankName(account.getBankName())
                .accountNumber(account.getAccountNumber())
                .accountHolder(account.getAccountHolder())
                .balance(account.getBalance())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }
}