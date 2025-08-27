package com.example.waitingreservationservice.controller;

import com.example.waitingreservationservice.dto.response.SpotResponse;
import com.example.waitingreservationservice.service.SpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/spots")
public class SpotController {

    private final SpotService spotService;

    @GetMapping("/health")
    public String healthCheck() {
        return "Spot Service is running";
    }

    @GetMapping("/{spotId}")
    public ResponseEntity<SpotResponse> getSpot(
            @PathVariable Long spotId
    ) {
        return ResponseEntity.ok(spotService.getSpot(spotId));
    }
}
