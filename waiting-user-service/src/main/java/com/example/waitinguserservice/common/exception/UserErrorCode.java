package com.example.waitinguserservice.common.exception;

import com.example.waitingcommon.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "해당 사용자를 찾을 수 없습니다."),
    EMAIL_NOT_EMPTY(HttpStatus.BAD_REQUEST, "EMAIL_NOT_EMPTY", "이메일은 필수 입력 값 입니다."),
    ALREADY_EXIST_EMAIL(HttpStatus.CONFLICT, "ALREADY_EXIST_EMAIL", "이미 사용중인 이메일입니다."),
    PASSWORD_NOT_EMPTY(HttpStatus.BAD_REQUEST, "PASSWORD_NOT_EMPTY", "비밀번호는 필수 입력 값 입니다."),
    INVALID_PASSWORD_FORMAT(HttpStatus.BAD_REQUEST, "INVALID_PASSWORD_FORMAT", "유효하지 않은 비밀번호 형식입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_REFRESH_TOKEN", "유효하지 않은 리프레시 토큰입니다."),
    ;

    private final HttpStatus status;
    private final String errorCode;
    private final String errorMessage;

    UserErrorCode(HttpStatus status, String errorCode, String errorMessage) {
        this.status = status;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getStatus() {
        return status.value();
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
