package com.billit.user_service.account.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountBorrowRequest {

    @NotBlank(message = "은행명은 필수 입력값입니다")
    private String bankName;

    @NotBlank(message = "계좌번호는 필수 입력값입니다")
    private String accountNumber;

    @NotBlank(message = "예금주명은 필수 입력값입니다")
    private String accountHolder;

    @Builder
    public AccountBorrowRequest(String bankName, String accountNumber, String accountHolder) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
    }
}