package com.example.waitingreservationservice.common.exception;

import com.example.waitingcommon.exception.BusinessException;

public class InvalidPhoneNumberFormatException extends BusinessException {
    public InvalidPhoneNumberFormatException() {
        super(ReservationErrorCode.INVALID_PHONE_NUMBER);
    }

    public InvalidPhoneNumberFormatException(String message) {
        super(ReservationErrorCode.INVALID_PHONE_NUMBER, message);
    }
}
