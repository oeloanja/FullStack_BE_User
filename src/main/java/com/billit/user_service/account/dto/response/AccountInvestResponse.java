package com.billit.user_service.account.dto.response;

import com.billit.user_service.account.domain.entity.InvestAccount;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class AccountInvestResponse {
    private Long id;
    private String bankName;
    private String accountNumber;
    private String accountHolder;
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AccountInvestResponse of(InvestAccount account) {
        return AccountInvestResponse.builder()
                .id(account.getId())
                .bankName(account.getBankName())
                .accountNumber(account.getAccountNumber())
                .accountHolder(account.getAccountHolder())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }
}