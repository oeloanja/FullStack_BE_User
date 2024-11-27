package com.billit.user_service.user.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EmailVerificationResponse {
    private String email;
    private String message;
    private LocalDateTime expiryDate;

    public static EmailVerificationResponse of(String email, String message, LocalDateTime expiryDate) {
        return EmailVerificationResponse.builder()
                .email(email)
                .message(message)
                .expiryDate(expiryDate)
                .build();
    }
}