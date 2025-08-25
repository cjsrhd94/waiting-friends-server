package com.example.waitingreservationservice.service;

import com.example.waitingreservationservice.common.annotation.DistributedLock;
import com.example.waitingreservationservice.common.exception.EnterNotAllowException;
import com.example.waitingreservationservice.common.exception.InvalidReservationStatusException;
import com.example.waitingreservationservice.common.util.RedisUtil;
import com.example.waitingreservationservice.dto.request.ReservationUpdateRequest;
import com.example.waitingreservationservice.dto.response.ReservationResponse;
import com.example.waitingreservationservice.entity.Reservation;
import com.example.waitingreservationservice.entity.Spot;
import com.example.waitingreservationservice.repository.ReservationRepository;

import com.example.waitingreservationservice.repository.reader.ReservationReader;
import com.example.waitingreservationservice.repository.reader.SpotReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationReader reservationReader;
    private final SpotReader spotReader;
    private final RedisUtil redisUtil;

    private final static String SPOT_CACHE_KEY = "spot::";

    @Transactional
    @DistributedLock(key = "'spot-'.concat(#spotId)")
    public Long reserve(
            Long spotId,
            String phoneNumber,
            Integer headCount
    ) {
        Spot spot = spotReader.findById(spotId);

        if (!spot.canWait()) {
            throw new EnterNotAllowException("현재 입장할 수 없는 상태입니다.");
        }

        Reservation reservation = new Reservation(spotId, phoneNumber, headCount);
        Long reservationId = reservationRepository.save(reservation).getId();

        spot.decreaseRemainingCapacity(headCount);

        redisUtil.addZSet(SPOT_CACHE_KEY + spotId, reservationId.toString(), System.currentTimeMillis());

        return reservationId;
    }

    @Transactional
    @DistributedLock(key = "'spot-'.concat(#spotId)")
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
    public ReservationResponse getReservation(Long reservationId) {
        Reservation reservation = reservationReader.findById(reservationId);
        return new ReservationResponse(reservation);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> getReservationsBySpot(Long spotId) {
        List<Reservation> reservations = reservationReader.getReservationsBySpotId(spotId);
        return reservations.stream()
                .map(ReservationResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public Long getWaitingOrder(Long reservationId, Long spotId) {
        Long cacheOrder = redisUtil.getZRank(SPOT_CACHE_KEY + spotId, String.valueOf(reservationId));

        if (cacheOrder != null) {
            return cacheOrder + 1;
        }

        return reservationReader.getReservationBySpotIdAndStatus(spotId, Reservation.Status.WAITING)
                .stream()
                .map(Reservation::getId)
                .filter(i -> i.equals(reservationId))
                .findFirst()
                .orElseThrow(() -> new InvalidReservationStatusException("해당 예약은 대기 상태가 아닙니다."))
                + 1L;
    }
}
