package com.example.waitingreservationservice.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ReservationOrderResponse {
    private Integer order;

    private ReservationOrderResponse(Integer order) {
        this.order = order;
    }

    public static ReservationOrderResponse of(Integer order) {
        return new ReservationOrderResponse(order);
    }
}
