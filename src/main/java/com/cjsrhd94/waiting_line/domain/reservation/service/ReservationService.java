package com.cjsrhd94.waiting_line.domain.reservation.service;

import com.cjsrhd94.waiting_line.domain.reservation.entity.Reservation;
import com.cjsrhd94.waiting_line.domain.reservation.repository.ReservationRepository;
import com.cjsrhd94.waiting_line.domain.spot.repository.reader.SpotReader;
import com.cjsrhd94.waiting_line.global.annotation.DistributedLock;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final SpotReader spotReader;

    @Transactional
    @DistributedLock(key = "#spotId")
    public Long reserve(
            Long spotId,
            String phoneNumber,
            Integer headCount
    ) {
        if ( !spotReader.existsById(spotId) ) {
            throw new IllegalArgumentException(spotId + "번 스팟이 존재하지 않습니다.");
        }

        Reservation reservation = new Reservation(spotId, phoneNumber, headCount);
        return reservationRepository.save(reservation).getId();
    }
}
