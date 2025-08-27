package com.example.waitingreservationservice.service;

import com.example.waitingreservationservice.common.annotation.DistributedLock;
import com.example.waitingreservationservice.common.util.RedisUtil;
import com.example.waitingreservationservice.dto.request.ReservationUpdateRequest;
import com.example.waitingreservationservice.dto.response.ReservationResponse;
import com.example.waitingreservationservice.entity.Reservation;
import com.example.waitingreservationservice.entity.Spot;
import com.example.waitingreservationservice.repository.reader.ReservationReader;
import com.example.waitingreservationservice.repository.reader.SpotReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.waitingreservationservice.common.util.RedisUtil.SPOT_CACHE_KEY;

@Service
@RequiredArgsConstructor
public class AdminReservationService {
    private final ReservationReader reservationReader;
    private final SpotReader spotReader;
    private final RedisUtil redisUtil;


    @Transactional
    @DistributedLock(key = "'spot-' + #spotId")
    public void updateStatus(
            Long reservationId,
            ReservationUpdateRequest request
    ) {
        Reservation reservation = reservationReader.findById(reservationId);

        reservation.updateStatus(request.getStatus());

        if (reservation.isCancelled()) {
            Spot spot = spotReader.findById(reservation.getSpotId());
            spot.increaseRemainingCapacity(reservation.getHeadCount());
        }

        redisUtil.removeZSet(SPOT_CACHE_KEY + reservation.getSpotId(), reservationId.toString());
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> getReservationsBySpot(Long spotId) {
        List<Reservation> reservations = reservationReader.getReservationsBySpotId(spotId);
        return reservations.stream()
                .map(ReservationResponse::new)
                .toList();
    }
}
