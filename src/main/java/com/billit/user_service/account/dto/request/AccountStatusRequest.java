package com.billit.user_service.account.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountStatusRequest {
    private boolean isDeleted = true;  // 삭제 상태로 변경
}