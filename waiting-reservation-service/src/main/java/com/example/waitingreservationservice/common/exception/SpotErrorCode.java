package com.example.waitingreservationservice.common.exception;

import com.example.waitingcommon.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum SpotErrorCode implements ErrorCode {
    SPOT_NOT_FOUND(HttpStatus.NOT_FOUND, "SPOT_NOT_FOUND", "해당 장소를 찾을 수 없습니다."),
    SPOT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "SPOT_ACCESS_DENIED", "해당 장소에 접근할 수 있는 권한이 없습니다."),
    NOT_ENOUGH_CAPACITY(HttpStatus.BAD_REQUEST, "NOT_ENOUGH_CAPACITY", "해당 장소의 남은 수용 인원이 부족합니다."),
    INVALID_SPOT_STATUS(HttpStatus.BAD_REQUEST, "INVALID_SPOT_STATUS", "유효하지 않은 장소 상태입니다."),
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
