// UserBorrowRepository.java
package com.billit.user_service.user.domain.repository;

import com.billit.user_service.user.domain.entity.UserBorrow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBorrowRepository extends JpaRepository<UserBorrow, Long> {
    Optional<UserBorrow> findByEmail(String email);
    boolean existsByEmail(String email);
}