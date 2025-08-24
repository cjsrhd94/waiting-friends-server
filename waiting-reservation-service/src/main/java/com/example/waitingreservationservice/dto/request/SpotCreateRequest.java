package com.example.waitingreservationservice.dto.request;

import lombok.Data;

@Data
public class SpotCreateRequest {
    private String name;
    private Integer capacity;
    private String address;
}
