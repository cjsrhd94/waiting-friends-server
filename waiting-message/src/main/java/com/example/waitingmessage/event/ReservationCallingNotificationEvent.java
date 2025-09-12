package com.example.waitingmessage.event;

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
}
