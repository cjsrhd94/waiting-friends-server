package com.example.waitingspotservice.dto.response;

import lombok.Data;

@Data
public class SpotResponse {
    private Long id;
    private String name;
    private Integer capacity;
    private String status;

    public SpotResponse(
            Long id,
            String name,
            Integer capacity,
            String status
    ) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.status = status;
    }
}
