package com.billit.user_service.account.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class WithdrawRequest {
    @NotNull(message = "계좌번호는 필수입니다")
    private Integer accountId;

    @NotNull(message = "출금액은 필수입니다")
    @Positive(message = "출금액은 양수여야 합니다")
    private BigDecimal amount;

    private String description;
}