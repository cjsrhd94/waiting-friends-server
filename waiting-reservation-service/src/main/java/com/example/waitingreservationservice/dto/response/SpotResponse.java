package com.example.waitingreservationservice.dto.response;

import com.example.waitingreservationservice.entity.Spot;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpotResponse {
    private Long id;
    private String name;
    private String address;
    private String status;

    public SpotResponse(
            Long id,
            String name,
            String address,
            String status
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.status = status;
    }

    public static SpotResponse from(Spot spot) {
        return new SpotResponse(
                spot.getId(),
                spot.getName(),
                spot.getAddress(),
                spot.getStatus().name()
        );
    }
}
