package com.example.waitingcommon.exception;

public interface ErrorCode {
    int getStatusCode();
    String getErrorCode();
    String getErrorMessage();
}
