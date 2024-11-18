package com.billit.user_service.user.dto.response;

import com.billit.user_service.user.domain.entity.UserBorrow;
import com.billit.user_service.user.domain.entity.UserInvest;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyPageResponse {
    private String email;
    private String userName;
    private String phone;

    // UserBorrow
    public static MyPageResponse of(UserBorrow user) {
        return MyPageResponse.builder()
                .email(user.getEmail())
                .userName(user.getUserName())
                .phone(user.getPhone())
                .build();
    }

    // UserInvest
    public static MyPageResponse of(UserInvest user) {
        return MyPageResponse.builder()
                .email(user.getEmail())
                .userName(user.getUserName())
                .phone(user.getPhone())
                .build();
    }
}