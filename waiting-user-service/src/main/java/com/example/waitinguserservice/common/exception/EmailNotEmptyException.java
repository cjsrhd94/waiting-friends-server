package com.example.waitinguserservice.common.exception;

import com.example.waitingcommon.exception.BusinessException;

public class EmailNotEmptyException extends BusinessException {
    public EmailNotEmptyException() {
        super(UserErrorCode.EMAIL_NOT_EMPTY);
    }

    public EmailNotEmptyException(String message) {
        super(UserErrorCode.EMAIL_NOT_EMPTY, message);
    }
}
