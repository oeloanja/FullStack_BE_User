package com.billit.user_service.user.domain.repository;

import com.billit.user_service.user.domain.entity.MasterCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MasterCodeRepository extends JpaRepository<MasterCode, Long> {
    Optional<MasterCode> findByCode(String code);
    boolean existsByCode(String code);
}