package com.billit.user_service.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e, WebRequest request) {
        String errorPosition = e.getStackTrace()[0].toString();

        log.error("""
                =============== CustomException ===============
                DateTime: {}
                Request: {}
                Code: {}
                Message: {}
                Position: {}
                =============================================""",
                LocalDateTime.now(),
                request.getDescription(false),
                e.getErrorCode().getCode(),
                e.getErrorCode().getMessage(),
                errorPosition
        );

        ErrorResponse response = ErrorResponse.builder()
                .code(e.getErrorCode().getCode())
                .message(e.getErrorCode().getMessage())
                .build();

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e, WebRequest request) {
        log.error("""
                =============== ValidationException ===============
                DateTime: {}
                Request: {}
                Error: {}
                ================================================""",
                LocalDateTime.now(),
                request.getDescription(false),
                e.getBindingResult().getAllErrors()
        );

        ErrorResponse response = ErrorResponse.builder()
                .code(ErrorCode.INVALID_INPUT_VALUE.getCode())
                .message(ErrorCode.INVALID_INPUT_VALUE.getMessage())
                .build();

        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT_VALUE.getStatus())
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e, WebRequest request) {
        String errorPosition = e.getStackTrace()[0].toString();

        log.error("""
                =============== UnexpectedException ===============
                DateTime: {}
                Request: {}
                Error Type: {}
                Error Message: {}
                Position: {}
                Stack Trace: {}
                ==================================================""",
                LocalDateTime.now(),
                request.getDescription(false),
                e.getClass().getSimpleName(),
                e.getMessage(),
                errorPosition,
                e.getStackTrace()
        );

        ErrorResponse response = ErrorResponse.builder()
                .code(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
                .message(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())
                .build();

        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(response);
    }
}