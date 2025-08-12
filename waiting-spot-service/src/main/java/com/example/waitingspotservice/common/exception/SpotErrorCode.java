package com.example.waitingspotservice.common.exception;

import com.example.waitingcommon.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum SpotErrorCode implements ErrorCode {
    SPOT_NOT_FOUND(HttpStatus.NOT_FOUND, "SPOT_NOT_FOUND", "해당 장소를 찾을 수 없습니다."),
    NOT_ENOUGH_CAPACITY(HttpStatus.BAD_REQUEST, "NOT_ENOUGH_CAPACITY", "해당 장소의 남은 수용 인원이 부족합니다."),
    ;
    private final HttpStatus status;
    private final String errorCode;
    private final String errorMessage;

    SpotErrorCode(HttpStatus status, String errorCode, String errorMessage) {
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
