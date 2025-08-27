package com.example.waitingreservationservice.controller;

import com.example.waitingreservationservice.dto.request.ReservationUpdateRequest;
import com.example.waitingreservationservice.dto.response.ReservationResponse;
import com.example.waitingreservationservice.service.AdminReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/reservations")
public class AdminReservationController {
    private final AdminReservationService adminReservationService;

    @PutMapping("/{reservationId}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long reservationId,
            @RequestBody ReservationUpdateRequest request
    ) {
        adminReservationService.updateStatus(reservationId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservationsBySpot(@RequestParam Long spotId) {
        return ResponseEntity.ok(adminReservationService.getReservationsBySpot(spotId));
    }
}
