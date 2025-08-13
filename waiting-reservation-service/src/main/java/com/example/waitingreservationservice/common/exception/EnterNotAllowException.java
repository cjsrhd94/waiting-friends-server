package com.example.waitingreservationservice.common.exception;

import com.example.waitingcommon.exception.BusinessException;

public class EnterNotAllowException extends BusinessException {
    public EnterNotAllowException() {
        super(ReservationErrorCode.ENTER_NOT_ALLOW);
    }

    public EnterNotAllowException(String message) {
        super(ReservationErrorCode.ENTER_NOT_ALLOW, message);
    }
}
