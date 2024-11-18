package com.billit.user_service.account.domain.entity;

import com.billit.user_service.common.config.entity.BaseTimeEntity;
import com.billit.user_service.user.domain.entity.UserBorrow;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "borrow_bank_accounts")
public class BorrowAccount extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String bankName;

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String accountHolder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_borrow_id")
    private UserBorrow userBorrow;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Builder
    public BorrowAccount(String bankName, String accountNumber,
                         String accountHolder, UserBorrow userBorrow) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.userBorrow = userBorrow;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
