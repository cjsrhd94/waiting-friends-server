package com.example.waitingreservationservice.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpotStatusUpdateRequest {
    private String status;

    public SpotStatusUpdateRequest(String status) {
        this.status = status;
    }

    public static SpotStatusUpdateRequest of (String status) {
        return new SpotStatusUpdateRequest(status);
    }
}
