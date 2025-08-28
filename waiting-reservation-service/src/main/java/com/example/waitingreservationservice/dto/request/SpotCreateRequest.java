package com.example.waitingreservationservice.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpotCreateRequest {
    private String name;
    private Integer capacity;
    private String address;

    public SpotCreateRequest(
            String name,
            Integer capacity,
            String address
    ) {
        this.name = name;
        this.capacity = capacity;
        this.address = address;
    }

    public static SpotCreateRequest of (
            String name,
            Integer capacity,
            String address
    ) {
        return new SpotCreateRequest(name, capacity, address);
    }
}
