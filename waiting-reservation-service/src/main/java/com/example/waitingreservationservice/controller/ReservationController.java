package com.example.waitingreservationservice.controller;

import com.example.waitingreservationservice.dto.request.ReservationCreateRequest;
import com.example.waitingreservationservice.dto.response.ReservationOrderResponse;
import com.example.waitingreservationservice.dto.response.ReservationResponse;
import com.example.waitingreservationservice.service.EventProducer;
import com.example.waitingreservationservice.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final EventProducer eventProducer;

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
    public ResponseEntity<Void> reserveWithKafka(@RequestBody ReservationCreateRequest request) {
        eventProducer.produceReservationEvent(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{reservationId}/status/cancel")
    public ResponseEntity<Void> cancel(
            @PathVariable Long reservationId
    ) {
        reservationService.cancel(reservationId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationResponse> getReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.getReservation(reservationId));
    }

    @GetMapping("/{reservationId}/order")
    public ResponseEntity<ReservationOrderResponse> getWaitingOrder(
            @PathVariable Long reservationId,
            @RequestParam Long spotId
    ) {
        return ResponseEntity.ok(reservationService.getWaitingOrder(reservationId, spotId));
    }
}
