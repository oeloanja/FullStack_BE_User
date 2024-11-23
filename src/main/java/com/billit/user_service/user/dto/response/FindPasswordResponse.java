package com.billit.user_service.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindPasswordResponse {
    private String userType;  // "BORROWER" 또는 "INVESTOR"
    private String tempPassword;
    private String email;  // 마스킹된 이메일
}