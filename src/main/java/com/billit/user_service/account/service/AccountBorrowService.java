package com.billit.user_service.account.service;

import com.billit.user_service.account.domain.entity.BorrowAccount;
import com.billit.user_service.account.domain.repository.BorrowAccountRepository;
import com.billit.user_service.account.dto.request.AccountBorrowRequest;
import com.billit.user_service.account.dto.response.AccountBorrowResponse;
import com.billit.user_service.common.exception.CustomException;
import com.billit.user_service.common.exception.ErrorCode;
import com.billit.user_service.user.domain.entity.UserBorrow;
import com.billit.user_service.user.domain.repository.UserBorrowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountBorrowService {

    private final BorrowAccountRepository borrowAccountRepository;
    private final UserBorrowRepository userBorrowRepository;

    // 계좌 등록
    @Transactional
    public AccountBorrowResponse createAccount(Long userId, AccountBorrowRequest request) {
        UserBorrow user = userBorrowRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (borrowAccountRepository.existsByAccountNumberAndIsDeletedFalse(request.getAccountNumber())) {
            throw new CustomException(ErrorCode.ACCOUNT_ALREADY_REGISTERED);
        }

        BorrowAccount account = BorrowAccount.builder()
                .userBorrow(user)
                .bankName(request.getBankName())
                .accountNumber(request.getAccountNumber())
                .accountHolder(request.getAccountHolder())
                .build();

        return AccountBorrowResponse.of(borrowAccountRepository.save(account));
    }

    // 계좌 목록 조회
    public List<AccountBorrowResponse> getAccounts(Long userId) {
        return borrowAccountRepository.findAllByUserBorrowIdAndIsDeletedFalse(userId)
                .stream()
                .map(AccountBorrowResponse::of)
                .collect(Collectors.toList());
    }

    // 계좌 삭제 (상태 변경)
    @Transactional
    public void deleteAccount(Long userId, Long accountId) {
        BorrowAccount account = borrowAccountRepository.findByIdAndIsDeletedFalse(accountId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (!account.getUserBorrow().getId().equals(userId)) {
            throw new CustomException(ErrorCode.ACCOUNT_USER_MISMATCH);
        }

        account.delete();
    }
}