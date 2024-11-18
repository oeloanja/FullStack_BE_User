package com.billit.user_service.user.dto.response;

import com.billit.user_service.user.domain.entity.UserBorrow;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserBorrowResponse {
    private Long id;
    private String email;
    private String userName;
    private String phone;
    private Integer creditRating;

    public static UserBorrowResponse of(UserBorrow userBorrow) {
        return UserBorrowResponse.builder()
                .id(userBorrow.getId())
                .email(userBorrow.getEmail())
                .userName(userBorrow.getUserName())
                .phone(userBorrow.getPhone())
                .creditRating(userBorrow.getCreditRating())
                .build();
    }
}