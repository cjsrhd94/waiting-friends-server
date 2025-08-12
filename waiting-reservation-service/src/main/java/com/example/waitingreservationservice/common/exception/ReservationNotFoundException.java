package com.example.waitingreservationservice.common.exception;

import com.example.waitingcommon.exception.BusinessException;

public class ReservationNotFoundException extends BusinessException {

    public ReservationNotFoundException() {
        super(ReservationErrorCode.RESERVATION_NOT_FOUND);
    }

    public ReservationNotFoundException(String message) {
        super(ReservationErrorCode.RESERVATION_NOT_FOUND, message);
    }
}
