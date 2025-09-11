package com.example.waitingreservationservice.dto.event;

import com.example.waitingreservationservice.entity.Reservation;
import com.example.waitingreservationservice.entity.Spot;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationWaitingNotificationEvent {
    private Long reservationId;
    private Long spotId;
    // 지점 이름
    private String spotName;
    // 전화번호
    private String phoneNumber;
    // 예약 인원
    private Integer headCount;
    // 예약 번호
    private Integer waitingNumber;
    // 예약 대기 순서
    private Integer waitingOrder;
    // 예상 대기 시간
    private Integer expectedWaitingTime;

    public ReservationWaitingNotificationEvent(
            Spot spot,
            Reservation reservation
    ) {
        this.reservationId = reservation.getId();
        this.spotId = spot.getId();
        this.spotName = spot.getName();
        this.phoneNumber = reservation.getPhoneNumber();
        this.headCount = reservation.getHeadCount();
        this.waitingNumber = 0;
        this.waitingOrder = 0;
        this.expectedWaitingTime = 0;
    }
}
