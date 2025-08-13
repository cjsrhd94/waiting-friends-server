package com.example.waitingreservationservice.common.exception;

import com.example.waitingcommon.exception.BusinessException;

public class InvalidHeadCountException extends BusinessException {
    public InvalidHeadCountException() {
        super(ReservationErrorCode.INVALID_HEAD_COUNT);
    }

    public InvalidHeadCountException(String message) {
        super(ReservationErrorCode.INVALID_HEAD_COUNT, message);
    }
}
