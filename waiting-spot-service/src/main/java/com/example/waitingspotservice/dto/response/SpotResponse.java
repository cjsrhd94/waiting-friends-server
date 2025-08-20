package com.example.waitingspotservice.dto.response;

import lombok.Data;

@Data
public class SpotResponse {
    private Long id;
    private String name;
    private String address;
    private Integer maxCapacity;
    private Integer remainingCapacity;
    private String status;

    public SpotResponse(
            Long id,
            String name,
            String address,
            Integer maxCapacity,
            Integer remainingCapacity,
            String status
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.maxCapacity = maxCapacity;
        this.remainingCapacity = remainingCapacity;
        this.status = status;
    }
}
