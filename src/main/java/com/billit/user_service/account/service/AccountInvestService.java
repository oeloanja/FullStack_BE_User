package com.billit.user_service.account.service;

import com.billit.user_service.account.domain.entity.InvestAccount;
import com.billit.user_service.account.domain.repository.InvestAccountRepository;
import com.billit.user_service.account.dto.request.AccountInvestRequest;
import com.billit.user_service.account.dto.response.AccountInvestResponse;
import com.billit.user_service.common.exception.CustomException;
import com.billit.user_service.common.exception.ErrorCode;
import com.billit.user_service.user.domain.entity.UserInvest;
import com.billit.user_service.user.domain.repository.UserInvestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountInvestService {

    private final InvestAccountRepository InvestAccountRepository;
    private final UserInvestRepository userInvestRepository;

    // 계좌 등록
    @Transactional
    public AccountInvestResponse createAccount(Long userId, AccountInvestRequest request) {
        UserInvest user = userInvestRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (InvestAccountRepository.existsByAccountNumberAndIsDeletedFalse(request.getAccountNumber())) {
            throw new CustomException(ErrorCode.ACCOUNT_ALREADY_REGISTERED);
        }

        InvestAccount account = InvestAccount.builder()
                .userInvest(user)
                .bankName(request.getBankName())
                .accountNumber(request.getAccountNumber())
                .accountHolder(request.getAccountHolder())
                .build();

        return AccountInvestResponse.of(InvestAccountRepository.save(account));
    }

    // 계좌 목록 조회
    public List<AccountInvestResponse> getAccounts(Long userId) {
        return InvestAccountRepository.findAllByUserInvestIdAndIsDeletedFalse(userId)
                .stream()
                .map(AccountInvestResponse::of)
                .collect(Collectors.toList());
    }

    // 계좌 삭제 (상태 변경)
    @Transactional
    public void deleteAccount(Long userId, Long accountId) {
        InvestAccount account = InvestAccountRepository.findByIdAndIsDeletedFalse(accountId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (!account.getUserInvest().getId().equals(userId)) {
            throw new CustomException(ErrorCode.ACCOUNT_USER_MISMATCH);
        }

        account.delete();
    }
}