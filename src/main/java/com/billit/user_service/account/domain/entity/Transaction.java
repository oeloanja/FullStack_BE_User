package com.billit.user_service.account.domain.entity;

import com.billit.user_service.account.domain.entity.enums.TransactionStatus;
import com.billit.user_service.account.domain.entity.enums.TransactionType;
import com.billit.user_service.common.config.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String transactionId;  // 거래 고유 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrow_account_id")
    private BorrowAccount borrowAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invest_account_id")
    private InvestAccount investAccount;

    @Column(nullable = false)
    private BigDecimal amount;  // 거래 금액

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;  // DEPOSIT, WITHDRAW, TRANSFER

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;  // PENDING, COMPLETED, FAILED

    private String description;  // 거래 설명

    @Builder
    public Transaction(String transactionId, BigDecimal amount,
                       TransactionType type, String description,
                       BorrowAccount borrowAccount, InvestAccount investAccount) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.status = TransactionStatus.PENDING;
        this.borrowAccount = borrowAccount;
        this.investAccount = investAccount;
    }

    // 거래 상태 변경 메서드
    public void complete() {
        this.status = TransactionStatus.COMPLETED;
    }

    public void fail() {
        this.status = TransactionStatus.FAILED;
    }

    // 계좌 설정 메서드
    public void setBorrowAccount(BorrowAccount borrowAccount) {
        this.borrowAccount = borrowAccount;
    }

    public void setInvestAccount(InvestAccount investAccount) {
        this.investAccount = investAccount;
    }
}