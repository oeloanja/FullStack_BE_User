package com.billit.user_service.common.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
    private final String code;
    private final String message;
    private final String timestamp;
    private final String path;
}