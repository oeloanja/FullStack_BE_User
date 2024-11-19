package com.billit.user_service.account.domain.repository;

import com.billit.user_service.account.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByBorrowAccountId(Long accountId);
    List<Transaction> findByInvestAccountId(Long accountId);
    List<Transaction> findByTransactionId(String transactionId);
}