package com.billit.user_service.account.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class TransferRequest {
    @NotNull(message = "출금 계좌번호는 필수입니다")
    private String fromAccountNumber;

    @NotNull(message = "입금 계좌번호는 필수입니다")
    private String toAccountNumber;

    @NotNull(message = "거래금액은 필수입니다")
    @Positive(message = "거래금액은 양수여야 합니다")
    private BigDecimal amount;

    private String description;
}