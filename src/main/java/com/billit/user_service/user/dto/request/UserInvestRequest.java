package com.billit.user_service.user.dto.request;

import com.billit.user_service.common.exception.CustomException;
import com.billit.user_service.common.exception.ErrorCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInvestRequest {

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "이메일 형식이 올바르지 않습니다. (예: example@email.com)")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 8자 이상, 영문, 숫자, 특수문자를 포함해야 합니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수 입력값입니다.")
    private String passwordConfirm;

    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String userName;

    @NotBlank(message = "전화번호는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$",
            message = "올바른 전화번호 형식이 아닙니다. (예: 010-1234-5678)")
    private String phone;

    @Builder
    public UserInvestRequest(String email, String password, String passwordConfirm,
                             String userName, String phone) {
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.userName = userName;
        this.phone = phone;
    }

    public void validatePassword() {
        if (!password.equals(passwordConfirm)) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCHED);
        }
    }
}