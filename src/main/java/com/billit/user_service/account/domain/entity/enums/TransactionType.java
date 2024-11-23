// TransactionType.java
package com.billit.user_service.account.domain.entity.enums;

public enum TransactionType {
    DEPOSIT("입금"),
    WITHDRAW("출금"),
    TRANSFER("송금");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}