package com.example.waitingreservationservice.common.exception;

import com.example.waitingcommon.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum ReservationErrorCode implements ErrorCode {
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "RESERVATION_NOT_FOUND", "해당 예약을 찾을 수 없습니다."),
    INVALID_RESERVATION_STATUS(HttpStatus.BAD_REQUEST, "INVALID_RESERVATION_STATUS", "유효하지 않은 예약 상태입니다."),
    INVALID_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "INVALID_PHONE_NUMBER", "유효하지 않은 전화번호 형식입니다."),
    INVALID_HEAD_COUNT(HttpStatus.BAD_REQUEST, "INVALID_HEAD_COUNT", "유효하지 않은 인원 수입니다."),
    ;

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
