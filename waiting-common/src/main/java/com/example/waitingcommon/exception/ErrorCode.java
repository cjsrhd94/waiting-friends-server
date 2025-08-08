package com.example.waitingcommon.exception;

public interface ErrorCode {
    int getStatus();
    String getErrorCode();
    String getErrorMessage();
}
