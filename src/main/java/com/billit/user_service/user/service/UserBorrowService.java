package com.billit.user_service.user.service;

import com.billit.user_service.common.exception.CustomException;
import com.billit.user_service.common.exception.ErrorCode;
import com.billit.user_service.user.domain.entity.UserBorrow;
import com.billit.user_service.user.domain.repository.UserBorrowRepository;
import com.billit.user_service.user.dto.request.LoginRequest;
import com.billit.user_service.user.dto.request.UserBorrowRequest;
import com.billit.user_service.user.dto.response.UserBorrowResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserBorrowService {

    private final UserBorrowRepository userBorrowRepository;

    @Transactional
    public UserBorrowResponse createUser(UserBorrowRequest request) {
        // 비밀번호 일치 여부 확인
        request.validatePassword();

        if (userBorrowRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        UserBorrow userBorrow = UserBorrow.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .userName(request.getUserName())
                .phone(request.getPhone())
                .build();

        UserBorrow savedUser = userBorrowRepository.save(userBorrow);
        return UserBorrowResponse.of(savedUser);
    }
    // 로그인 메서드
    @Transactional(readOnly = true)
    public UserBorrowResponse login(LoginRequest request) {
        UserBorrow user = userBorrowRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        return UserBorrowResponse.of(user);


    }
}
