package com.billit.user_service.user.domain.entity;

import com.billit.user_service.common.config.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Entity
@Table(name = "user_invest")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInvest extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_investor_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(name = "user_name")
    private String userName;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(name = "investment_grade", nullable = false, length = 10)
    private String investmentGrade;

    @Builder
    public UserInvest(String email, String password, String userName,
                      String phone, String investmentGrade) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.phone = phone;
        this.investmentGrade = investmentGrade;
    }

    public void updateInvestmentGrade(String investmentGrade) {
        this.investmentGrade = investmentGrade;
    }
}