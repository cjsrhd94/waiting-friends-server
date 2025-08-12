package com.example.waitinguserservice.common.exception;

import com.example.waitingcommon.exception.BusinessException;

public class InvalidPasswordFormatException extends BusinessException {
    public InvalidPasswordFormatException() {
        super(UserErrorCode.INVALID_PASSWORD_FORMAT);
    }

    public InvalidPasswordFormatException(String message) {
        super(UserErrorCode.INVALID_PASSWORD_FORMAT, message);
    }
}
