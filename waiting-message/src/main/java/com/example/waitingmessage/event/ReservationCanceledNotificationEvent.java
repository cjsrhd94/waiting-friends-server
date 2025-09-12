package com.example.waitingmessage.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationCanceledNotificationEvent {
    private Long reservationId;
    private Long spotId;
    // 지점 이름
    private String spotName;
    // 전화번호
    private String phoneNumber;
}
