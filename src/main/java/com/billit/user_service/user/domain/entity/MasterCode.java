package com.billit.user_service.user.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "master_codes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MasterCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 4, unique = true)
    private String code;

    @Column(nullable = false)
    private boolean isUsed;

    @Builder
    public MasterCode(String code) {
        this.code = code;
        this.isUsed = true;
    }

    public static final String MASTER_CODE = "0000";
}
