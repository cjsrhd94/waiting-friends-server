package com.example.waitingcommon.response;

import com.example.waitingcommon.exception.ErrorCode;

public class ErrorResponse {
    private final int status;
    private final String errorCode;
    private final String errorMessage;

    public ErrorResponse(ErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.errorCode = errorCode.getErrorCode();
        this.errorMessage = errorCode.getErrorMessage();
    }

    public ErrorResponse(ErrorCode errorCode, String errorMessage) {
        this.status = errorCode.getStatus();
        this.errorCode = errorCode.getErrorCode();
        this.errorMessage = errorMessage;
    }

    public ErrorResponse(int status, String errorCode, String errorMessage) {
        this.status = status;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
