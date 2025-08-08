package com.example.waitinguserservice.common.exception;

import com.example.waitingcommon.exception.BusinessException;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException() {
        super(UserErrorCode.USER_NOT_FOUND);
    }

    public UserNotFoundException(String message) {
        super(UserErrorCode.USER_NOT_FOUND, message);
    }
}
