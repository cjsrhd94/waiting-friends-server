package com.cjsrhd94.waiting_line.domain.reservation.controller;

import com.cjsrhd94.waiting_line.domain.reservation.dto.request.ReservationCreateRequest;
import com.cjsrhd94.waiting_line.domain.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public Long reserve(@RequestBody ReservationCreateRequest request) {
        return reservationService.reserve(
                request.getSpotId(),
                request.getPhoneNumber(),
                request.getHeadCount()
        );
    }
}
