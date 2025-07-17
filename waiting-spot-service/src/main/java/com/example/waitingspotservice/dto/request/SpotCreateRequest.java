package com.example.waitingspotservice.dto.request;

import com.example.waitingspotservice.entity.Spot;
import lombok.Data;

@Data
public class SpotCreateRequest {
    private String name;
    private Integer capacity;

    public Spot toEntity() {
        return Spot.builder()
                .name(name)
                .capacity(capacity)
                .build();
    }
}
