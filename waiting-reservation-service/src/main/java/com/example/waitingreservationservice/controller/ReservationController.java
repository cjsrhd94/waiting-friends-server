package com.example.waitingreservationservice.controller;

import com.example.waitingreservationservice.dto.request.ReservationCreateRequest;
import com.example.waitingreservationservice.dto.request.ReservationUpdateRequest;
import com.example.waitingreservationservice.dto.response.ReservationResponse;
import com.example.waitingreservationservice.service.ReservationProducer;
import com.example.waitingreservationservice.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Void> reserve(@RequestBody ReservationCreateRequest request) {
        reservationService.reserve(request.getSpotId(), request.getPhoneNumber(), request.getHeadCount());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/kafka")
    public ResponseEntity<Void> reserveByKafka(@RequestBody ReservationCreateRequest request) {
        reservationProducer.produceReservationEvent(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{reservationId}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long reservationId,
            @RequestBody ReservationUpdateRequest request
    ) {
        reservationService.updateStatus(reservationId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationResponse> getReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.getReservation(reservationId));
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservationsBySpot(@RequestParam Long spotId) {
        return ResponseEntity.ok(reservationService.getReservationsBySpot(spotId));
    }

    @GetMapping("/{reservationId}/order")
    public ResponseEntity<Long> getWaitingOrder(
            @PathVariable Long reservationId,
            @RequestParam Long spotId
    ) {
        return ResponseEntity.ok(reservationService.getWaitingOrder(reservationId, spotId));
    }
}
