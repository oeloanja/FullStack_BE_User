package com.billit.user_service.user.domain.entity;

import com.billit.user_service.common.config.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import java.time.LocalDate;

@Entity
@Table(name = "user_borrow")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserBorrow extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_borrow_id")
    private Long id;

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

    @Builder
    public UserBorrow(String email, String password, String userName, String phone) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.phone = phone;
    }

    public void updateCreditRating(Integer creditRating) {
        this.creditRating = creditRating;
        this.creditRatingDate = LocalDate.now();
    }
}