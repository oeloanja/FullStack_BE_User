package com.billit.user_service.user.dto.response;

import com.billit.user_service.account.dto.response.AccountBorrowResponse;
import com.billit.user_service.user.domain.entity.UserBorrow;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class UserBorrowResponse {
    private UUID userBorrowId;
    private String email;
    private String password;
    private String userName;
    private String phone;
    private Integer creditRating;
    private String borrowmentGrade;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<AccountBorrowResponse> accounts;  // 계좌 정보 추가

    public static UserBorrowResponse of(UserBorrow userBorrow) {
        return UserBorrowResponse.builder()
                .userBorrowId(userBorrow.getId())
                .email(userBorrow.getEmail())
                .userName(userBorrow.getUserName())
                .phone(userBorrow.getPhone())
                .creditRating(userBorrow.getCreditRating())
                .borrowmentGrade(userBorrow.getBorrowmentGrade())
                .createdAt(userBorrow.getCreatedAt())
                .updatedAt(userBorrow.getUpdatedAt())
                .build();
    }
}