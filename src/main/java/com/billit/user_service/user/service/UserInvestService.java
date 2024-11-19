package com.billit.user_service.user.service;

import com.billit.user_service.common.exception.CustomException;
import com.billit.user_service.common.exception.ErrorCode;
import com.billit.user_service.user.domain.entity.UserInvest;
import com.billit.user_service.user.domain.repository.UserInvestRepository;
import com.billit.user_service.user.dto.request.LoginRequest;
import com.billit.user_service.user.dto.request.PasswordUpdateRequest;
import com.billit.user_service.user.dto.request.PhoneUpdateRequest;
import com.billit.user_service.user.dto.request.UserInvestRequest;
import com.billit.user_service.user.dto.response.MyPageResponse;
import com.billit.user_service.user.dto.response.UserInvestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserInvestService {

    private final UserInvestRepository userInvestRepository;

    // 회원가입
    @Transactional
    public UserInvestResponse createUser(UserInvestRequest request) {
        // 비밀번호 일치 여부 확인
        request.validatePassword();

        if (userInvestRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        UserInvest userInvest = UserInvest.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .userName(request.getUserName())
                .phone(request.getPhone())
                .build();

        UserInvest savedUser = userInvestRepository.save(userInvest);
        return UserInvestResponse.of(savedUser);
    }

    // 로그인
    public UserInvestResponse login(LoginRequest request) {
        UserInvest user = userInvestRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        return UserInvestResponse.of(user);
    }

    // 마이페이지 조회
    public MyPageResponse getMyPage(Long userId) {
        UserInvest user = userInvestRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return MyPageResponse.of(user);
    }

    // 비밀번호 변경
    @Transactional
    public void updatePassword(Long userId, PasswordUpdateRequest request) {
        UserInvest user = userInvestRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!user.getPassword().equals(request.getCurrentPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCHED);
        }

        if (!request.getNewPassword().equals(request.getNewPasswordConfirm())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCHED);
        }

        user.updatePassword(request.getNewPassword());
    }

    public UserInvestResponse getUserInfo(Long userId) {
        UserInvest user = userInvestRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return UserInvestResponse.of(user);
    }



    // 전화번호 변경
    @Transactional
    public void updatePhone(Long userId, PhoneUpdateRequest request) {
        UserInvest user = userInvestRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.updatePhone(request.getPhone());
    }
}