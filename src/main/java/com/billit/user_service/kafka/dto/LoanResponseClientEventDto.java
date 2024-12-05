package com.billit.user_service.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanResponseClientEventDto {
    private Integer loanId;
    private Integer groupId;
    private Integer accountBorrowId;
    private Integer userBorrowId;
    private BigDecimal loanAmount;
    private Integer term;
    private BigDecimal intRate;
}
