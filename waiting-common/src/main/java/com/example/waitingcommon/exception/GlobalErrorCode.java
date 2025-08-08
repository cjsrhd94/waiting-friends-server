package com.example.waitingcommon.exception;

public enum GlobalErrorCode implements ErrorCode {
    MISSING_PARAMETER(400, "MISSING_PARAMETER", "필요한 파라미터 값이 제공되지 않았습니다."),
    INVALID_PARAMETER(400, "INVALID_PARAMETER", "파라미터 값이 유효하지 않습니다."),
    INVALID_CONTENT_TYPE(415, "INVALID_CONTENT_TYPE", "API가 요구하는 Content-Type과 다릅니다."),
    API_NOT_FOUND(404, "API_NOT_FOUND", "API가 요구하는 URL 혹은 HTTP 메서드와 다릅니다."),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "정의되지 않은 서버 오류가 발생했습니다.")
    ;

    private final int status;
    private final String errorCode;
    private final String errorMessage;

    GlobalErrorCode(int status, String errorCode, String errorMessage) {
        this.status = status;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
