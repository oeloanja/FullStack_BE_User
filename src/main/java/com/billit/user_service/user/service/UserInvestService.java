package com.billit.user_service.user.service;

import com.billit.user_service.account.domain.repository.BorrowAccountRepository;
import com.billit.user_service.account.domain.repository.InvestAccountRepository;
import com.billit.user_service.account.dto.response.AccountInvestResponse;
import com.billit.user_service.common.exception.CustomException;
import com.billit.user_service.common.exception.ErrorCode;
import com.billit.user_service.security.jwt.JwtTokenProvider;
import com.billit.user_service.user.domain.entity.UserInvest;
import com.billit.user_service.user.domain.repository.UserInvestRepository;
import com.billit.user_service.user.dto.request.LoginRequest;
import com.billit.user_service.user.dto.request.PasswordUpdateRequest;
import com.billit.user_service.user.dto.request.PhoneUpdateRequest;
import com.billit.user_service.user.dto.request.UserInvestRequest;
import com.billit.user_service.user.dto.response.LoginResponse;
import com.billit.user_service.user.dto.response.MyPageResponse;
import com.billit.user_service.user.dto.response.UserInvestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserInvestService {

    private final UserInvestRepository userInvestRepository;
    private final InvestAccountRepository investAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입
    @Transactional
    public UserInvestResponse createUser(UserInvestRequest request) {
        request.validatePassword();

        if (userInvestRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        UserInvest userInvest = UserInvest.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))  // 암호화
                .userName(request.getUserName())
                .phone(request.getPhone())
                .build();

        UserInvest savedUser = userInvestRepository.save(userInvest);
        return UserInvestResponse.of(savedUser);
    }

    // 로그인
    public LoginResponse<UserInvestResponse> login(LoginRequest request) {
        UserInvest user = userInvestRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        String token = jwtTokenProvider.createToken(user.getEmail(), "ROLE_INVESTOR");

        return LoginResponse.of(token, UserInvestResponse.of(user));
    }

    // 마이페이지 조회
    public MyPageResponse getMyPage(Long userId) {
        UserInvest user = userInvestRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return MyPageResponse.of(user);
    }

    // 사용자 정보 조회
    public UserInvestResponse getUserInfo(Long userId) {
        UserInvest user = userInvestRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 해당 사용자의 계좌 정보 조회
        List<AccountInvestResponse> accounts = investAccountRepository
                .findAllByUserInvestIdAndIsDeletedFalse(userId)
                .stream()
                .map(AccountInvestResponse::of)
                .collect(Collectors.toList());

        return UserInvestResponse.builder()
                .userInvestId(user.getId())
                .email(user.getEmail())
                .userName(user.getUserName())
                .phone(user.getPhone())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .accounts(accounts)
                .build();
    }

    // 비밀번호 변경
    @Transactional
    public void updatePassword(Long userId, PasswordUpdateRequest request) {
        UserInvest user = userInvestRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCHED);
        }

        if (!request.getNewPassword().equals(request.getNewPasswordConfirm())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCHED);
        }

        user.updatePassword(passwordEncoder.encode(request.getNewPassword()));
    }

    // 전화번호 변경
    @Transactional
    public void updatePhone(Long userId, PhoneUpdateRequest request) {
        UserInvest user = userInvestRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.updatePhone(request.getPhone());
    }
}