// UserBorrowRepository.java
package com.billit.user_service.user.domain.repository;

import com.billit.user_service.user.domain.entity.UserBorrow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserBorrowRepository extends JpaRepository<UserBorrow, UUID> {
    Optional<UserBorrow> findByEmail(String email);
    boolean existsByEmail(String email);
}