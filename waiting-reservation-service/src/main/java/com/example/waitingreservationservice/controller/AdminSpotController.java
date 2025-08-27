package com.example.waitingreservationservice.controller;

import com.example.waitingreservationservice.dto.request.SpotCreateRequest;
import com.example.waitingreservationservice.dto.request.StatusUpdateRequest;
import com.example.waitingreservationservice.dto.response.SpotResponse;
import com.example.waitingreservationservice.service.AdminSpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/spots")
public class AdminSpotController {
    private final AdminSpotService adminSpotService;

    @PostMapping
    public ResponseEntity<Long> createSpot(
            @RequestHeader("id") Long userId,
            @RequestBody SpotCreateRequest request
    ) {
        return ResponseEntity.ok(adminSpotService.createSpot(userId, request));
    }

    @PutMapping("/{spotId}/status")
    public ResponseEntity<Void> updateSpotStatus(
            @PathVariable Long spotId,
            @RequestBody StatusUpdateRequest request
    ) {
        adminSpotService.updateSpotStatus(spotId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{spotId}")
    public ResponseEntity<SpotResponse> getSpot(
            @RequestHeader("id") Long userId,
            @PathVariable Long spotId
    ) {
        return ResponseEntity.ok(adminSpotService.getSpot(userId, spotId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<SpotResponse>> getSpotsBySearch(
            @RequestParam String address
    ) {
        return ResponseEntity.ok(adminSpotService.getSpotsBySearch(address));
    }

    @PostMapping("/dummy")
    public ResponseEntity<Void> bulkInsertDummySpots(@RequestParam Integer num) throws  InterruptedException {
        adminSpotService.bulkInsertDummySpots(num);
        return ResponseEntity.ok().build();
    }
}
