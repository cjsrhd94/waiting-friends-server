package com.example.waitingreservationservice.repository.reader;

import com.example.waitingreservationservice.entity.Reservation;
import com.example.waitingreservationservice.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationReader {
    private final ReservationRepository reservationRepository;

    public Reservation findById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException(reservationId + "번 예약이 존재하지 않습니다."));
    }

}
