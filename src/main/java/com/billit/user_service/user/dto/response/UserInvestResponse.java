package com.billit.user_service.user.dto.response;

import com.billit.user_service.account.dto.response.AccountInvestResponse;
import com.billit.user_service.user.domain.entity.UserInvest;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class UserInvestResponse {
    private Long id;
    private String email;
    private String userName;
    private String phone;
    private Integer creditRating;
    private String InvestmentGrade;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<AccountInvestResponse> accounts;  // 계좌 정보 추가

    public static UserInvestResponse of(UserInvest userInvest) {
        return UserInvestResponse.builder()
                .id(userInvest.getId())
                .email(userInvest.getEmail())
                .userName(userInvest.getUserName())
                .phone(userInvest.getPhone())
                .InvestmentGrade(userInvest.getInvestmentGrade())
                .createdAt(userInvest.getCreatedAt())
                .updatedAt(userInvest.getUpdatedAt())
                .build();
    }
}