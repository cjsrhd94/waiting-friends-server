package com.example.waitingspotservice.dto.response;

import lombok.Data;

@Data
public class SpotResponse {
    private Long id;
    private String name;
    private Integer maxCapacity;
    private Integer remainingCapacity;
    private String status;

    public SpotResponse(
            Long id,
            String name,
            Integer maxCapacity,
            Integer remainingCapacity,
            String status
    ) {
        this.id = id;
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.remainingCapacity = remainingCapacity;
        this.status = status;
    }
}
