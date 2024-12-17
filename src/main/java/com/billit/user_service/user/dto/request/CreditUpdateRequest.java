package com.billit.user_service.user.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreditUpdateRequest {
    private String phoneNumber;
    private Integer target;
}
