package com.billit.user_service.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C002", "지원하지 않는 메서드입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C003", "서버 에러가 발생했습니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "C004", "잘못된 타입입니다."),

    // User 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을 수 없습니다."),
    INVALID_USER_STATUS(HttpStatus.BAD_REQUEST, "U002", "유효하지 않은 사용자 상태입니다."),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "U003", "이미 존재하는 사용자입니다."),
    USER_AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "U004", "사용자 인증에 실패했습니다."),
    PASSWORD_NOT_MATCHED(HttpStatus.BAD_REQUEST, "U003", "비밀번호가 일치하지 않습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "U004", "비밀번호가 일치하지 않습니다."),
    INVALID_USER_INFO(HttpStatus.BAD_REQUEST, "U005", "사용자 정보가 일치하지 않습니다."),

    // 이메일 관련
    EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "E001", "이메일 발송에 실패했습니다."),
    INVALID_EMAIL_INFO(HttpStatus.BAD_REQUEST, "E002", "잘못된 이메일 정보입니다."),
    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "E003", "잘못된 인증 코드입니다."),
    VERIFICATION_CODE_EXPIRED(HttpStatus.BAD_REQUEST, "E004", "만료된 인증 코드입니다."),
    ALREADY_VERIFIED(HttpStatus.BAD_REQUEST, "E005", "이미 인증이 완료되었습니다."),
    VERIFICATION_REQUIRED(HttpStatus.BAD_REQUEST, "E006", "이메일 인증이 필요합니다."),

    // 마스터 코드
    INVALID_MASTER_CODE(HttpStatus.UNAUTHORIZED, "M001", "유효하지 않은 마스터 코드입니다."),

    // Account 관련
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "A001", "계좌를 찾을 수 없습니다."),
    INSUFFICIENT_BALANCE(HttpStatus.BAD_REQUEST, "A002", "잔액이 부족합니다."),
    ACCOUNT_ALREADY_REGISTERED(HttpStatus.CONFLICT, "A003", "이미 등록된 계좌입니다."),
    INVALID_ACCOUNT_STATUS(HttpStatus.BAD_REQUEST, "A004", "유효하지 않은 계좌 상태입니다."),
    ACCOUNT_VERIFICATION_FAILED(HttpStatus.BAD_REQUEST, "A005", "계좌 인증에 실패했습니다."),
    ACCOUNT_USER_MISMATCH(HttpStatus.FORBIDDEN, "A006", "해당 계좌에 대한 권한이 없습니다."),

    // Borrow 관련
    BORROW_NOT_FOUND(HttpStatus.NOT_FOUND, "B001", "대출 정보를 찾을 수 없습니다."),
    BORROW_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "B002", "대출 한도를 초과했습니다."),
    INVALID_BORROW_STATUS(HttpStatus.BAD_REQUEST, "B003", "유효하지 않은 대출 상태입니다."),
    BORROW_REQUEST_FAILED(HttpStatus.BAD_REQUEST, "B004", "대출 신청에 실패했습니다."),
    BORROW_ALREADY_EXISTS(HttpStatus.CONFLICT, "B005", "이미 진행 중인 대출이 있습니다."),

    // Invest 관련
    INVEST_NOT_FOUND(HttpStatus.NOT_FOUND, "I001", "투자 정보를 찾을 수 없습니다."),
    INVEST_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "I002", "투자 한도를 초과했습니다."),
    INVALID_INVEST_STATUS(HttpStatus.BAD_REQUEST, "I003", "유효하지 않은 투자 상태입니다."),
    INVEST_ALREADY_CLOSED(HttpStatus.BAD_REQUEST, "I004", "이미 마감된 투자입니다."),
    INVEST_AMOUNT_TOO_SMALL(HttpStatus.BAD_REQUEST, "I005", "최소 투자 금액 미달입니다."),

    // Transaction 관련
    TRANSACTION_FAILED(HttpStatus.BAD_REQUEST, "T001", "거래 처리에 실패했습니다."),
    INVALID_TRANSACTION_STATUS(HttpStatus.BAD_REQUEST, "T002", "유효하지 않은 거래 상태입니다."),
    DUPLICATE_TRANSACTION(HttpStatus.CONFLICT, "T003", "중복된 거래입니다."),
    TRANSACTION_TIMEOUT(HttpStatus.REQUEST_TIMEOUT, "T004", "거래 시간이 초과되었습니다."),
    TRANSACTION_NOT_FOUND(HttpStatus.NOT_FOUND, "T005", "거래 정보를 찾을 수 없습니다."),

    // Balance 관련
    BALANCE_NOT_FOUND(HttpStatus.NOT_FOUND, "BL001", "잔액 정보를 찾을 수 없습니다."),
    BALANCE_UPDATE_FAILED(HttpStatus.BAD_REQUEST, "BL002", "잔액 업데이트에 실패했습니다."),
    NEGATIVE_BALANCE_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "BL003", "잔액이 음수가 될 수 없습니다."),

    // 리프레시 토큰 관련
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "RT001", "유효하지 않은 리프레시 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "RT002", "리프레시 토큰을 찾을 수 없습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "RT003", "만료된 리프레시 토큰입니다."),
    REFRESH_TOKEN_REVOKED(HttpStatus.UNAUTHORIZED, "RT004", "폐기된 리프레시 토큰입니다."),
    REFRESH_TOKEN_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "RT005", "리프레시 토큰 저장에 실패했습니다."),

    // 비밀번호 토큰
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "AUTH003", "접근 권한이 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}