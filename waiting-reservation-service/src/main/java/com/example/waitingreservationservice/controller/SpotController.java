package com.example.waitingreservationservice.controller;


import com.example.waitingreservationservice.dto.request.SpotCreateRequest;
import com.example.waitingreservationservice.dto.request.SpotRemainingCapacityRequest;
import com.example.waitingreservationservice.dto.request.StatusUpdateRequest;
import com.example.waitingreservationservice.dto.response.SpotResponse;
import com.example.waitingreservationservice.service.SpotService;
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

    @PutMapping("/{spotId}/status")
    public ResponseEntity<Void> updateSpotStatus(
            @PathVariable Long spotId,
            @RequestBody StatusUpdateRequest request
    ) {
        spotService.updateSpotStatus(spotId, request);
        return ResponseEntity.ok().build();
    }
}
