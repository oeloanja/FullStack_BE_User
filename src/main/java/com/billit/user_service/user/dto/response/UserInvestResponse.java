package com.billit.user_service.user.dto.response;

import com.billit.user_service.user.domain.entity.UserInvest;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInvestResponse {
    private Long id;
    private String email;
    private String userName;
    private String phone;
    private String investmentGrade;

    public static UserInvestResponse of(UserInvest userInvest) {
        return UserInvestResponse.builder()
                .id(userInvest.getId())
                .email(userInvest.getEmail())
                .userName(userInvest.getUserName())
                .phone(userInvest.getPhone())
                .investmentGrade(userInvest.getInvestmentGrade())
                .build();
    }
}