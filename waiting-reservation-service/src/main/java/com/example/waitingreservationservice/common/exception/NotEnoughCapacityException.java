package com.example.waitingreservationservice.common.exception;

import com.example.waitingcommon.exception.BusinessException;

public class NotEnoughCapacityException extends BusinessException {
    public NotEnoughCapacityException() {
        super(SpotErrorCode.NOT_ENOUGH_CAPACITY);
    }

    public NotEnoughCapacityException(String message) {
        super(SpotErrorCode.NOT_ENOUGH_CAPACITY, message);
    }
}
