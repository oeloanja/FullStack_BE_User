// UserInvestRepository.java
package com.billit.user_service.user.domain.repository;

import com.billit.user_service.user.domain.entity.UserInvest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserInvestRepository extends JpaRepository<UserInvest, UUID> {
    Optional<UserInvest> findByEmail(String email);
    boolean existsByEmail(String email);
}