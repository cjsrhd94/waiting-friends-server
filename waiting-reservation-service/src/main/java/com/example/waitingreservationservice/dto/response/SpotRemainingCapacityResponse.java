package com.example.waitingreservationservice.dto.response;

import lombok.Data;

@Data
public class SpotRemainingCapacityResponse {
    private Integer remainingCapacity;

    public SpotRemainingCapacityResponse(Integer remainingCapacity) {
        this.remainingCapacity = remainingCapacity;
    }
}
