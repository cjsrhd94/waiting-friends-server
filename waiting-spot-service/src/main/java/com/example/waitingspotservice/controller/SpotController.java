package com.example.waitingspotservice.controller;

import com.example.waitingspotservice.dto.request.SpotCreateRequest;
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
        return "Waiting Spot Service is running";
    }

    @PostMapping
    public ResponseEntity<Long> createSpot(
            @RequestBody SpotCreateRequest request
    ) {
        return ResponseEntity.ok(spotService.createSpot(request));
    }

    @GetMapping("/{spotId}/status")
    public ResponseEntity<Boolean> canWaiting(
            @PathVariable Long spotId
    ) {
        return ResponseEntity.ok(spotService.canWaiting(spotId));
    }
}
