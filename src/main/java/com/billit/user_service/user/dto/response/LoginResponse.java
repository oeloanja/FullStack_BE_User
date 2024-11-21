package com.billit.user_service.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse<T> {
    private String accessToken;
    private String refreshToken;
    private T user;

    public static <T> LoginResponse<T> of(String accessToken, String refreshToken, T user) {
        return LoginResponse.<T>builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(user)
                .build();
    }
}