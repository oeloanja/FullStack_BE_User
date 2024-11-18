package com.billit.user_service.account.domain.repository;

import com.billit.user_service.account.domain.entity.BorrowAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BorrowAccountRepository extends JpaRepository<BorrowAccount, Long> {
    List<BorrowAccount> findAllByUserBorrowIdAndIsDeletedFalse(Long userBorrowId);
    Optional<BorrowAccount> findByIdAndIsDeletedFalse(Long id);
    boolean existsByAccountNumberAndIsDeletedFalse(String accountNumber);
}