package com.billit.user_service.account.domain.repository;

import com.billit.user_service.account.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByBorrowAccountId(Integer accountId);
    List<Transaction> findByInvestAccountId(Integer accountId);
    List<Transaction> findByTransactionId(String transactionId);
}