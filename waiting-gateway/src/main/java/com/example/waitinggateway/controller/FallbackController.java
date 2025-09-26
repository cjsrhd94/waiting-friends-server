package com.example.waitinggateway.controller;

import com.example.waitingcommon.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping
    public ResponseEntity<ErrorResponse> fallback() {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "SERVICE_UNAVAILABLE",
                "서비스를 일시적으로 사용할 수 없습니다. 잠시 후 다시 시도해 주세요."
        );

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(errorResponse);
    }

    @GetMapping("/user-service")
    public ResponseEntity<ErrorResponse> userServiceFallback() {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "USER_SERVICE_UNAVAILABLE",
                "User 서비스를 일시적으로 사용할 수 없습니다. 잠시 후 다시 시도해 주세요."
        );

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(errorResponse);
    }

    @GetMapping("/reservation-service")
    public ResponseEntity<ErrorResponse> reservationServiceFallback() {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "RESERVATION_SERVICE_UNAVAILABLE",
                "Reservation 서비스를 일시적으로 사용할 수 없습니다. 잠시 후 다시 시도해 주세요."
        );

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(errorResponse);
    }

    @GetMapping("/spot-service")
    public ResponseEntity<ErrorResponse> spotServiceFallback() {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "SPOT_SERVICE_UNAVAILABLE",
                "Spot 서비스를 일시적으로 사용할 수 없습니다. 잠시 후 다시 시도해 주세요."
        );

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(errorResponse);
    }
}

