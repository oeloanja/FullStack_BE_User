package com.billit.user_service.account.domain.entity;

import com.billit.user_service.common.config.entity.BaseTimeEntity;
import com.billit.user_service.user.domain.entity.UserInvest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "Invest_bank_accounts")
public class InvestAccount extends BaseTimeEntity {

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
    @JoinColumn(name = "user_Invest_id")
    private UserInvest userInvest;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Builder
    public InvestAccount(String bankName, String accountNumber,
                         String accountHolder, UserInvest userInvest) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.userInvest = userInvest;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
