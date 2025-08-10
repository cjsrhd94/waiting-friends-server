package com.example.waitingreservationservice.dto.response;

import com.example.waitingreservationservice.entity.Reservation;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationResponse {
    private Long id;
    private Long spotId;
    private String phoneNumber;
    private Integer headCount;
    private String status;
    private LocalDateTime reservationDate;

    public ReservationResponse(
            Reservation reservation
    ) {
        this.id = reservation.getId();
        this.spotId = reservation.getSpotId();
        this.phoneNumber = reservation.getPhoneNumber();
        this.headCount = reservation.getHeadCount();
        this.status = reservation.getStatus().toString();
        this.reservationDate = reservation.getReservationDate();
    }
}
