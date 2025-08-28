package com.example.waitingreservationservice.dto.response;

import com.example.waitingreservationservice.entity.Reservation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationResponse {
    private Long id;
    private Long spotId;
    private String spotName;
    private String phoneNumber;
    private Integer headCount;
    private String status;
    private LocalDateTime reservationDate;

    private ReservationResponse(
            Long id,
            Long spotId,
            String spotName,
            String phoneNumber,
            Integer headCount,
            String status,
            LocalDateTime reservationDate
    ) {
        this.id = id;
        this.spotId = spotId;
        this.spotName = spotName;
        this.phoneNumber = phoneNumber;
        this.headCount = headCount;
        this.status = status;
        this.reservationDate = reservationDate;
    }

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getSpotId(),
                reservation.getSpotName(),
                reservation.getPhoneNumber(),
                reservation.getHeadCount(),
                reservation.getStatus().name(),
                reservation.getReservationDate()
        );
    }
}
