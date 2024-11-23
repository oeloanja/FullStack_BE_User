package com.billit.user_service.security.jwt;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false)
    private boolean revoked = false;  // isRevoked -> revoked로 변경

    @Builder
    public RefreshToken(String token, String userEmail, LocalDateTime expiryDate) {
        this.token = token;
        this.userEmail = userEmail;
        this.expiryDate = expiryDate;
        this.revoked = false;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    // isRevoked getter
    public boolean isRevoked() {
        return revoked;
    }
}