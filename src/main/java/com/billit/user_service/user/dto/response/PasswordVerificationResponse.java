package com.billit.user_service.user.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PasswordVerificationResponse {
    private String verificationToken;
    private LocalDateTime expiryTime;
}