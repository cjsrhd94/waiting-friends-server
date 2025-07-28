package com.example.waitingreservationservice.controller;

import com.example.waitingreservationservice.dto.request.ReservationCreateRequest;
import com.example.waitingreservationservice.dto.response.ReservationResponse;
import com.example.waitingreservationservice.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping("/health")
    public String healthCheck() {
        return "Reservation Service is running";
    }

    @PostMapping
    public Long reserve(@RequestBody ReservationCreateRequest request) {
        return reservationService.reserve(
                request.getSpotId(),
                request.getPhoneNumber(),
                request.getHeadCount()
        );
    }

    @GetMapping("/{reservationId}")
    public ReservationResponse getReservation(@PathVariable Long reservationId) {
        return reservationService.getReservation(reservationId);
    }
}
