package com.billit.user_service.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse<T> {
    private String token;
    private T user;

    public static <T> LoginResponse<T> of(String token, T user) {
        return LoginResponse.<T>builder()
                .token(token)
                .user(user)
                .build();
    }
}