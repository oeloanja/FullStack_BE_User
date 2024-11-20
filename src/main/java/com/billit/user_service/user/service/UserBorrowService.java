package com.billit.user_service.user.service;

import com.billit.user_service.account.domain.repository.BorrowAccountRepository;
import com.billit.user_service.account.dto.response.AccountBorrowResponse;
import com.billit.user_service.common.exception.CustomException;
import com.billit.user_service.common.exception.ErrorCode;
import com.billit.user_service.security.jwt.JwtTokenProvider;
import com.billit.user_service.user.domain.entity.UserBorrow;
import com.billit.user_service.user.domain.repository.UserBorrowRepository;
import com.billit.user_service.user.dto.request.LoginRequest;
import com.billit.user_service.user.dto.request.PasswordUpdateRequest;
import com.billit.user_service.user.dto.request.PhoneUpdateRequest;
import com.billit.user_service.user.dto.request.UserBorrowRequest;
import com.billit.user_service.user.dto.response.LoginResponse;
import com.billit.user_service.user.dto.response.MyPageResponse;
import com.billit.user_service.user.dto.response.UserBorrowResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserBorrowService {

    private final UserBorrowRepository userBorrowRepository;
    private final BorrowAccountRepository borrowAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입 - 비밀번호 암호화 추가
    @Transactional
    public UserBorrowResponse createUser(UserBorrowRequest request) {
        request.validatePassword();

        if (userBorrowRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        UserBorrow userBorrow = UserBorrow.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))  // 암호화
                .userName(request.getUserName())
                .phone(request.getPhone())
                .build();

        UserBorrow savedUser = userBorrowRepository.save(userBorrow);
        return UserBorrowResponse.of(savedUser);
    }

    // 로그인 - JWT 토큰 발급 추가
    public LoginResponse<UserBorrowResponse> login(LoginRequest request) {
        UserBorrow user = userBorrowRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        String token = jwtTokenProvider.createToken(user.getEmail(), "ROLE_BORROWER");

        return LoginResponse.of(token, UserBorrowResponse.of(user));
    }

    // 마이페이지 조회
    public MyPageResponse getMyPage(Long userId) {
        UserBorrow user = userBorrowRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return MyPageResponse.of(user);
    }

    // 비밀번호 변경 - 암호화된 비밀번호 비교
    @Transactional
    public void updatePassword(Long userId, PasswordUpdateRequest request) {
        UserBorrow user = userBorrowRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCHED);
        }

        if (!request.getNewPassword().equals(request.getNewPasswordConfirm())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCHED);
        }

        user.updatePassword(passwordEncoder.encode(request.getNewPassword()));
    }

    // 사용자 정보 조회
    public UserBorrowResponse getUserInfo(Long userId) {
        UserBorrow user = userBorrowRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 해당 사용자의 계좌 정보 조회
        List<AccountBorrowResponse> accounts = borrowAccountRepository
                .findAllByUserBorrowIdAndIsDeletedFalse(userId)
                .stream()
                .map(AccountBorrowResponse::of)
                .collect(Collectors.toList());

        return UserBorrowResponse.builder()
                .userBorrowId(user.getId())
                .email(user.getEmail())
                .userName(user.getUserName())
                .phone(user.getPhone())
                .creditRating(user.getCreditRating())
                .borrowmentGrade(user.getBorrowmentGrade())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .accounts(accounts)
                .build();
    }

    // 전화번호 변경
    @Transactional
    public void updatePhone(Long userId, PhoneUpdateRequest request) {
        UserBorrow user = userBorrowRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.updatePhone(request.getPhone());
    }
}
