// TransactionStatus.java
package com.billit.user_service.account.domain.entity.enums;

public enum TransactionStatus {
    PENDING("처리중"),
    COMPLETED("완료"),
    FAILED("실패");

    private final String description;

    TransactionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}