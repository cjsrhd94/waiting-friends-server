package com.example.waitingspotservice.controller;

import com.example.waitingspotservice.dto.request.SpotCreateRequest;
import com.example.waitingspotservice.dto.request.SpotRemainingCapacityRequest;
import com.example.waitingspotservice.dto.request.StatusUpdateRequest;
import com.example.waitingspotservice.dto.response.SpotResponse;
import com.example.waitingspotservice.service.SpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/spots")
public class SpotController {

    private final SpotService spotService;

    @GetMapping("/health")
    public String healthCheck() {
        return "Spot Service is running";
    }

    @PostMapping
    public ResponseEntity<Long> createSpot(
            @RequestHeader("id") Long userId,
            @RequestBody SpotCreateRequest request
    ) {
        return ResponseEntity.ok(spotService.createSpot(userId, request));
    }

    @GetMapping("/{spotId}")
    public ResponseEntity<SpotResponse> getSpot(
            @RequestHeader("id") Long userId,
            @PathVariable Long spotId
    ) {
        return ResponseEntity.ok(spotService.getSpot(userId, spotId));
    }

    @GetMapping("/{spotId}/status")
    public ResponseEntity<Boolean> canWait(
            @PathVariable Long spotId
    ) {
        return ResponseEntity.ok(spotService.canWait(spotId));
    }

    @PutMapping("/{spotId}/capacity/decrease")
    public ResponseEntity<Void> decreaseRemainingCapacity(
            @PathVariable Long spotId,
            @RequestBody SpotRemainingCapacityRequest request
    ) {
        spotService.decreaseRemainingCapacity(spotId, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{spotId}/capacity/increase")
    public ResponseEntity<Void> increaseRemainingCapacity(
            @PathVariable Long spotId,
            @RequestBody SpotRemainingCapacityRequest request
    ) {
        spotService.increaseRemainingCapacity(spotId, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{spotId}/status")
    public ResponseEntity<Void> updateSpotStatus(
            @PathVariable Long spotId,
            @RequestBody StatusUpdateRequest request
    ) {
        spotService.updateSpotStatus(spotId, request);
        return ResponseEntity.ok().build();
    }
}
