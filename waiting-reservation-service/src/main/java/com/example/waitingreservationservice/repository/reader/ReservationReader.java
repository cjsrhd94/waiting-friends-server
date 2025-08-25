package com.example.waitingreservationservice.repository.reader;

import com.example.waitingreservationservice.common.exception.ReservationNotFoundException;
import com.example.waitingreservationservice.entity.Reservation;
import com.example.waitingreservationservice.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationReader {
    private final ReservationRepository reservationRepository;

    public Reservation findById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId + "번 예약이 존재하지 않습니다."));
    }

    public List<Reservation> getReservationsBySpotId(Long spotId) {
        return reservationRepository.getReservationsBySpotId(spotId);
    }

    public List<Reservation> getReservationBySpotIdAndStatus(Long spotId, Reservation.Status status) {
        return reservationRepository.getReservationIdBySpotIdAndStatus(spotId, status);
    }
}
