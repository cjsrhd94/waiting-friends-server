package com.example.waitingreservationservice.common.exception;

import com.example.waitingcommon.exception.BusinessException;

public class SpotAccessDeniedException extends BusinessException {
    public SpotAccessDeniedException() {
        super(SpotErrorCode.SPOT_ACCESS_DENIED);
    }

    public SpotAccessDeniedException(String message) {
        super(SpotErrorCode.SPOT_ACCESS_DENIED, message);
    }
}
