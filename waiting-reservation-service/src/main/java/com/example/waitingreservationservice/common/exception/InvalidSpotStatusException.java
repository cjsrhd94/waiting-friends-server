package com.example.waitingreservationservice.common.exception;

import com.example.waitingcommon.exception.BusinessException;

public class InvalidSpotStatusException extends BusinessException {
    public InvalidSpotStatusException() {
        super(SpotErrorCode.INVALID_SPOT_STATUS);
    }

    public InvalidSpotStatusException(String message) {
        super(SpotErrorCode.INVALID_SPOT_STATUS, message);
    }
}
