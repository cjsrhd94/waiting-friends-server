package com.example.waitingspotservice.dto.request;

import lombok.Data;

@Data
public class SpotCreateRequest {
    private String name;
    private Integer capacity;
}
