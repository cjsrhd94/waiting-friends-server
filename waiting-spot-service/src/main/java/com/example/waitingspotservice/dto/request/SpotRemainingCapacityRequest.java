package com.example.waitingspotservice.dto.request;

import lombok.Data;

@Data
public class SpotRemainingCapacityRequest {
    private Integer headCount;

    public SpotRemainingCapacityRequest(Integer headCount) {
        this.headCount = headCount;
    }
}
