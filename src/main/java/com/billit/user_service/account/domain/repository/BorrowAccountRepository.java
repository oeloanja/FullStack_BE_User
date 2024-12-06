package com.billit.user_service.account.domain.repository;

import com.billit.user_service.account.domain.entity.BorrowAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BorrowAccountRepository extends JpaRepository<BorrowAccount, Integer> {
    List<BorrowAccount> findAllByUserBorrowIdAndIsDeletedFalse(UUID userBorrowId);
    Optional<BorrowAccount> findByIdAndIsDeletedFalse(Integer id);
    Optional<BorrowAccount> findByAccountNumberAndIsDeletedFalse(String accountNumber);
    boolean existsByAccountNumberAndIsDeletedFalse(String accountNumber);
}