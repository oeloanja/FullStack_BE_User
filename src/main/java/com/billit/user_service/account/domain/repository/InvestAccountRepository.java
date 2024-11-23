package com.billit.user_service.account.domain.repository;

import com.billit.user_service.account.domain.entity.InvestAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvestAccountRepository extends JpaRepository<InvestAccount, Long> {
    List<InvestAccount> findAllByUserInvestIdAndIsDeletedFalse(Long userInvestId);
    Optional<InvestAccount> findByIdAndIsDeletedFalse(Long id);
    Optional<InvestAccount> findByAccountNumberAndIsDeletedFalse(String accountNumber);
    boolean existsByAccountNumberAndIsDeletedFalse(String accountNumber);
}