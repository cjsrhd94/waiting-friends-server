package com.example.waitingreservationservice.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationUpdateRequest {
    private String status;

    public ReservationUpdateRequest(String status) {
        this.status = status;
    }

    public static ReservationUpdateRequest of (String status) {
        return new ReservationUpdateRequest(status);
    }
}
