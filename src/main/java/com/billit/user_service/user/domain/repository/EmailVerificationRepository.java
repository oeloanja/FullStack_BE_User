package com.billit.user_service.user.domain.repository;

import com.billit.user_service.user.domain.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    Optional<EmailVerification> findByEmailAndVerificationCode(String email, String code);
    Optional<EmailVerification> findTopByEmailOrderByExpiryDateDesc(String email);
    boolean existsByEmailAndVerified(String email, boolean verified);
}