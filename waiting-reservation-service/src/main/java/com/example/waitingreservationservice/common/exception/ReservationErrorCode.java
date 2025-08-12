package com.example.waitingreservationservice.common.exception;

import com.example.waitingcommon.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum ReservationErrorCode implements ErrorCode {
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "RESERVATION_NOT_FOUND", "해당 예약을 찾을 수 없습니다."),;

    private final HttpStatus status;
    private final String errorCode;
    private final String errorMessage;

    ReservationErrorCode(HttpStatus status, String errorCode, String errorMessage) {
        this.status = status;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getStatus() {
        return 0;
    }

    @Override
    public String getErrorCode() {
        return "";
    }

    @Override
    public String getErrorMessage() {
        return "";
    }
}
