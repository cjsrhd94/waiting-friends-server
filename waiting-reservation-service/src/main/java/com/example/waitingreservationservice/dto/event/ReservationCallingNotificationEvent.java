package com.example.waitingreservationservice.dto.event;

import com.example.waitingreservationservice.entity.Reservation;
import com.example.waitingreservationservice.entity.Spot;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationCallingNotificationEvent {
    private Long reservationId;
    private Long spotId;
    // 지점 이름
    private String spotName;
    // 전화번호
    private String phoneNumber;
    // 예약 번호
    private Integer waitingNumber;

    public ReservationCallingNotificationEvent(Reservation reservation) {
        this.reservationId = reservation.getId();
        this.spotId = reservation.getSpotId();
        this.spotName = reservation.getSpotName();
        this.phoneNumber = reservation.getPhoneNumber();
        this.waitingNumber = reservation.getWaitingNumber();
    }
}
