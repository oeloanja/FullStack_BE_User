package com.billit.user_service.user.domain.entity;

import com.billit.user_service.common.config.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "user_borrow")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserBorrow extends BaseTimeEntity {

    @Id
    @Column(name = "user_borrow_id", columnDefinition = "VARCHAR(36)")
    private UUID id = UUID.randomUUID();

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(name = "user_name")
    private String userName;

    @Column(nullable = false, length = 15)
    private String phone;

    @Column(name = "credit_rating")
    private Integer creditRating;

    @Column(name = "credit_rating_date")
    private LocalDate creditRatingDate;

    @Column(name = "borrowment_grade")
    private String borrowmentGrade;

    @Column(nullable = false)
    private boolean emailVerified = false;



    @Builder
    public UserBorrow(String email, String password, String userName, String phone) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.phone = phone;
        this.borrowmentGrade = "BASIC";  // 초기 등급 설정
        this.emailVerified = false;
    }

    public void updateCreditRating(Integer creditRating) {
        this.creditRating = creditRating;
        this.creditRatingDate = LocalDate.now();
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updatePhone(String newPhone) {
        this.phone = newPhone;
    }

    public void verifyEmail() {
        this.emailVerified = true;
    }
}
