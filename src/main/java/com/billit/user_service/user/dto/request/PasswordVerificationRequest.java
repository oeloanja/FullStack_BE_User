package com.billit.user_service.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PasswordVerificationRequest {
    @NotBlank(message = "비밀번호는 필수입니다")
    private String password;
}