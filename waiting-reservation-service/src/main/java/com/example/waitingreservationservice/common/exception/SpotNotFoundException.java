package com.example.waitingreservationservice.common.exception;

import com.example.waitingcommon.exception.BusinessException;

public class SpotNotFoundException extends BusinessException {
    public SpotNotFoundException() {
        super(SpotErrorCode.SPOT_NOT_FOUND);
    }

    public SpotNotFoundException(String message) {
        super(SpotErrorCode.SPOT_NOT_FOUND, message);
    }
}
