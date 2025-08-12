package com.example.waitingreservationservice.common.exception;

import com.example.waitingcommon.exception.BusinessException;
import com.example.waitingcommon.exception.GlobalErrorCode;
import com.example.waitingcommon.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

public class ReservationExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * API 호출 시 필수 parameter가 누락되었을 때 발생
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            HttpServletRequest request,
            MissingServletRequestParameterException e
    ) {
        logException(request, e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                GlobalErrorCode.MISSING_PARAMETER.getErrorCode(),
                                e.getParameterName() + "는 필수 값 입니다."
                        )
                );
    }

    /**
     * 유효한 parameter 값이 아닐 때 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleInvalidParameterException(
            HttpServletRequest request,
            MethodArgumentTypeMismatchException e
    ) {
        logException(request, e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                GlobalErrorCode.INVALID_PARAMETER.getErrorCode(),
                                e.getParameter() + "의 값이 유효하지 않습니다."
                        )
                );
    }

    /**
     * API가 요구하는 URL 혹은 HTTP 메서드와 다를 떄 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
        logException(request, e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        new ErrorResponse(
                                HttpStatus.NOT_FOUND.value(),
                                GlobalErrorCode.API_NOT_FOUND.getErrorCode(),
                                GlobalErrorCode.API_NOT_FOUND.getErrorMessage()
                        )
                );
    }

    /**
     * API가 요구하는 content-type과 다를 떄 발생
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(HttpServletRequest request, HttpMediaTypeNotSupportedException e) {
        logException(request, e);
        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                .body(
                        new ErrorResponse(
                                HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                                GlobalErrorCode.INVALID_CONTENT_TYPE.getErrorCode(),
                                GlobalErrorCode.INVALID_CONTENT_TYPE.getErrorMessage()
                        )
                );
    }


    /**
     * 비즈니스 로직에서 정의한 예외 발생
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(HttpServletRequest request, BusinessException e) {
        logException(request, e);

        if (e.getMessage() != null) {
            return ResponseEntity
                    .status(e.getErrorCode().getStatus())
                    .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
        }

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(new ErrorResponse(e.getErrorCode()));
    }

    /**
     * 정의되지 않은 서버 오류 발생
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(HttpServletRequest request, Exception e) {
        logException(request, e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                GlobalErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(),
                                GlobalErrorCode.INTERNAL_SERVER_ERROR.getErrorMessage()
                        )
                );
    }

    private void logException(HttpServletRequest request, Exception e) {
        logger.error(
                "[EXCEPTION] URI: {}, Method: {}, Type: {}, Message: {}",
                request.getRequestURI(),
                request.getMethod(),
                e.getClass().getSimpleName(),
                e.getMessage()
        );
    }
}
