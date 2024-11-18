package com.billit.user_service.account.dto.response;

import com.billit.user_service.account.domain.entity.BorrowAccount;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AccountBorrowResponse {
    private Long id;
    private String bankName;
    private String accountNumber;
    private String accountHolder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AccountBorrowResponse of(BorrowAccount account) {
        return AccountBorrowResponse.builder()
                .id(account.getId())
                .bankName(account.getBankName())
                .accountNumber(account.getAccountNumber())
                .accountHolder(account.getAccountHolder())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }
}