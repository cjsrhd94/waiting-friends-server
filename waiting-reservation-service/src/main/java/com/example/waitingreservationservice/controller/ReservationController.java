package com.example.waitingreservationservice.controller;

import com.example.waitingreservationservice.dto.request.ReservationCreateRequest;
import com.example.waitingreservationservice.dto.response.ReservationResponse;
import com.example.waitingreservationservice.service.ReservationProducer;
import com.example.waitingreservationservice.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final ReservationProducer reservationProducer;

    @GetMapping("/health")
    public String healthCheck() {
        return "Reservation Service is running";
    }

    @PostMapping
    public void reserve(@RequestBody ReservationCreateRequest request) {
        reservationProducer.produceReservationEvent(request);
    }

    @GetMapping("/{reservationId}")
    public ReservationResponse getReservation(@PathVariable Long reservationId) {
        return reservationService.getReservation(reservationId);
    }

    @GetMapping
    public List<ReservationResponse> getReservationsBySpot(@RequestParam Long spotId) {
        return reservationService.getReservationsBySpot(spotId);
    }
}
