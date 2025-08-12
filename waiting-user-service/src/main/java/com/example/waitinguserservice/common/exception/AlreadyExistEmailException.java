package com.example.waitinguserservice.common.exception;

import com.example.waitingcommon.exception.BusinessException;

public class AlreadyExistEmailException extends BusinessException {
    public AlreadyExistEmailException() {
        super(UserErrorCode.ALREADY_EXIST_EMAIL);
    }

    public AlreadyExistEmailException(String message) {
        super(UserErrorCode.ALREADY_EXIST_EMAIL, message);
    }
}
