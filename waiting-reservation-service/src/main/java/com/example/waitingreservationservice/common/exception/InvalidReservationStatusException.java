package com.example.waitingreservationservice.common.exception;

import com.example.waitingcommon.exception.BusinessException;

public class InvalidReservationStatusException extends BusinessException {
    public InvalidReservationStatusException() {
        super(ReservationErrorCode.INVALID_RESERVATION_STATUS);
    }

    public InvalidReservationStatusException(String message) {
        super(ReservationErrorCode.INVALID_RESERVATION_STATUS, message);
    }
}
