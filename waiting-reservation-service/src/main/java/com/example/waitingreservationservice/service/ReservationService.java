package com.example.waitingreservationservice.service;

import com.example.waitingreservationservice.client.SpotFeignClient;
import com.example.waitingreservationservice.common.annotation.DistributedLock;
import com.example.waitingreservationservice.dto.response.ReservationResponse;
import com.example.waitingreservationservice.entity.Reservation;
import com.example.waitingreservationservice.repository.ReservationRepository;

import com.example.waitingreservationservice.repository.reader.ReservationReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationReader reservationReader;
    private final SpotFeignClient spotFeignClient;

    @Transactional
    @DistributedLock(key = "#spotId")
    public Long reserve(
            Long spotId,
            String phoneNumber,
            Integer headCount
    ) {
        if ( !spotFeignClient.canWaiting(spotId) ) {
            throw new IllegalArgumentException("현재 입장할 수 없는 상태입니다.");
        }

        Reservation reservation = new Reservation(spotId, phoneNumber, headCount);
        return reservationRepository.save(reservation).getId();
    }

    @Transactional(readOnly = true)
    public ReservationResponse getReservation(Long reservationId) {
        Reservation reservation = reservationReader.findById(reservationId);
        return new ReservationResponse(reservation);
    }
}
