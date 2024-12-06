package com.billit.user_service.account.domain.repository;

import com.billit.user_service.account.domain.entity.InvestAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvestAccountRepository extends JpaRepository<InvestAccount, Integer> {
    List<InvestAccount> findAllByUserInvestIdAndIsDeletedFalse(UUID userInvestId);
    Optional<InvestAccount> findByIdAndIsDeletedFalse(Integer id);
    Optional<InvestAccount> findByAccountNumberAndIsDeletedFalse(String accountNumber);
    boolean existsByAccountNumberAndIsDeletedFalse(String accountNumber);
}