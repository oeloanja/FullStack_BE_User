package com.billit.user_service.user.service;

import com.billit.user_service.common.exception.CustomException;
import com.billit.user_service.common.exception.ErrorCode;
import com.billit.user_service.user.domain.entity.UserInvest;
import com.billit.user_service.user.domain.repository.UserInvestRepository;
import com.billit.user_service.user.dto.request.LoginRequest;
import com.billit.user_service.user.dto.request.UserInvestRequest;
import com.billit.user_service.user.dto.response.UserInvestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserInvestService {

    private final UserInvestRepository userInvestRepository;

    @Transactional
    public UserInvestResponse createUser(UserInvestRequest request) {
        // 비밀번호 일치 여부 확인
        request.validatePassword();

        if (userInvestRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        UserInvest userInvest = UserInvest.builder()
                .email(request.getEmail())
                .password(request.getPassword()) // 실제로는 암호화 필요
                .userName(request.getUserName())
                .phone(request.getPhone())
                .investmentGrade("BASIC") // 초기 등급
                .build();

        UserInvest savedUser = userInvestRepository.save(userInvest);
        return UserInvestResponse.of(savedUser);
    }

    // 로그인 메서드 추가
    @Transactional(readOnly = true)
    public UserInvestResponse login(LoginRequest request) {
        UserInvest user = userInvestRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        return UserInvestResponse.of(user);
    }
}