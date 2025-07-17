package com.example.waitingreservationservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "spot-service", url = "${reservation.server.spot}", path = "/api/v1/spots")
public interface SpotFeignClient {

    @GetMapping("/{spotId}/status")
    Boolean canWaiting(@PathVariable("spotId") Long spotId);
}
