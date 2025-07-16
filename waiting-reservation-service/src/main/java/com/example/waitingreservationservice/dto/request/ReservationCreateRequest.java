package com.example.waitingreservationservice.dto.request;

import lombok.Data;

@Data
public class ReservationCreateRequest {
    private Long spotId;
    private String phoneNumber;
    private Integer headCount;
}
