package com.example.waitinguserservice.common.exception;

import com.example.waitingcommon.exception.BusinessException;

public class InvalidRefreshTokenException extends BusinessException {
    public InvalidRefreshTokenException() {
        super(UserErrorCode.INVALID_REFRESH_TOKEN);
    }

    public InvalidRefreshTokenException(String message) {
        super(UserErrorCode.INVALID_REFRESH_TOKEN, message);
    }
}
