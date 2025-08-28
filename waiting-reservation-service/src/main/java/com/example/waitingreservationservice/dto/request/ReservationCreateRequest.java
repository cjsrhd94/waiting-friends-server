package com.example.waitingreservationservice.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationCreateRequest {
    private Long spotId;
    private String phoneNumber;
    private Integer headCount;

    public ReservationCreateRequest(
            Long spotId,
            String phoneNumber,
            Integer headCount
    ) {
        this.spotId = spotId;
        this.phoneNumber = phoneNumber;
        this.headCount = headCount;
    }

    public static ReservationCreateRequest of(
            Long spotId,
            String phoneNumber,
            Integer headCount
    ) {
        return new ReservationCreateRequest(spotId, phoneNumber, headCount);
    }
}
