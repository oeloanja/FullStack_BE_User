package com.billit.user_service.account.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GroupDepositRequest {
    @NotNull(message = "계좌 ID는 필수입니다.")
    private Integer userBorrowAccountId;

    @NotNull(message = "회원 ID는 필수입니다.")
    private Integer userBorrowId;

    @NotNull(message = "금액은 필수입니다.")
    @Positive(message = "금액은 양수여야 합니다.")
    private BigDecimal amount;

    @NotNull(message = "거래 설명은 필수입니다.")
    private String description;
}
