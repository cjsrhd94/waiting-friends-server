package com.cjsrhd94.waiting_line.domain.reservation.dto.request;

import lombok.Data;

@Data
public class ReservationCreateRequest {
    private Long spotId;
    private String phoneNumber;
    private Integer headCount;
}
