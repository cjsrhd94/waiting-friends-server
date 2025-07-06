package com.cjsrhd94.waiting_line.global.response;

public class ApiResponse<T> {
    private final boolean success;
    private final int code;
    private final String message;
    private final T data;

    public ApiResponse(boolean success, int code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(int code, String message, T data) {
        return new ApiResponse<>(true, code, message, data);
    }

    public static <T> ApiResponse<T> fail(int code, String message, T data) {
        return new ApiResponse<>(false, code, message, data);
    }
}
