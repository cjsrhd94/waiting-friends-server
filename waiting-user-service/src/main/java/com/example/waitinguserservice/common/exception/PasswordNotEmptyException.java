package com.example.waitinguserservice.common.exception;

import com.example.waitingcommon.exception.BusinessException;

public class PasswordNotEmptyException extends BusinessException {
    public PasswordNotEmptyException() {
        super(UserErrorCode.PASSWORD_NOT_EMPTY);
    }

    public PasswordNotEmptyException(String message) {
        super(UserErrorCode.PASSWORD_NOT_EMPTY, message);
    }
}
