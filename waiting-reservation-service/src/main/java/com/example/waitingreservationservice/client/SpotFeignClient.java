package com.example.waitingreservationservice.client;

import com.example.waitingreservationservice.dto.request.SpotRemainingCapacityRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "spot-service", url = "${reservation.server.spot}", path = "/api/v1/spots")
public interface SpotFeignClient {

    @GetMapping("/{spotId}/status")
    Boolean canWaiting(@PathVariable("spotId") Long spotId);

    @PutMapping("/{spotId}/capacity/decrease")
    Void decreaseRemainingCapacity(
            @PathVariable("spotId") Long spotId,
            SpotRemainingCapacityRequest request
    );

    @PutMapping("/{spotId}/capacity/increase")
    Void increaseRemainingCapacity(
            @PathVariable("spotId") Long spotId,
            SpotRemainingCapacityRequest request
    );
}
