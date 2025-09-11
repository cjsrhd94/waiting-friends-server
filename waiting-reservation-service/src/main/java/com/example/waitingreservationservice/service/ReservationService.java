package com.example.waitingreservationservice.service;

import com.example.waitingredis.util.RedisUtil;
import com.example.waitingredis.common.annotation.DistributedLock;
import com.example.waitingreservationservice.common.exception.EnterNotAllowException;
import com.example.waitingreservationservice.common.exception.InvalidReservationStatusException;
import com.example.waitingreservationservice.common.util.CacheKey;
import com.example.waitingreservationservice.common.util.EventProducer;
import com.example.waitingreservationservice.dto.event.ReservationWaitingNotificationEvent;
import com.example.waitingreservationservice.dto.response.ReservationOrderResponse;
import com.example.waitingreservationservice.dto.response.ReservationResponse;
import com.example.waitingreservationservice.entity.Reservation;
import com.example.waitingreservationservice.entity.Spot;
import com.example.waitingreservationservice.repository.ReservationRepository;

import com.example.waitingreservationservice.repository.reader.ReservationReader;
import com.example.waitingreservationservice.repository.reader.SpotReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationReader reservationReader;
    private final SpotReader spotReader;

    private final RedisUtil redisUtil;
    private final EventProducer eventProducer;

    @Transactional
    @DistributedLock(key = "'spot-' + #spotId")
    public Long reserve(
            Long spotId,
            String phoneNumber,
            Integer headCount
    ) {
        Spot spot = spotReader.findById(spotId);

        if (!spot.canWait()) {
            throw new EnterNotAllowException("현재 입장할 수 없는 상태입니다.");
        }

        Reservation reservation = new Reservation(spotId, spot.getName(), phoneNumber, headCount);
        Long reservationId = reservationRepository.save(reservation).getId();

        spot.decreaseRemainingCapacity(headCount);

        redisUtil.addZSet(CacheKey.SPOT.getKey() + spotId, reservationId.toString(), System.currentTimeMillis());

        eventProducer.sendReservationWaiting(new ReservationWaitingNotificationEvent(spot, reservation));

        return reservationId;
    }

    @Transactional
    @DistributedLock(key = "'spot-' + #spotId")
    public void cancel(Long reservationId) {
        Reservation reservation = reservationReader.findById(reservationId);
        reservation.cancel();

        Spot spot = spotReader.findById(reservation.getSpotId());
        spot.increaseRemainingCapacity(reservation.getHeadCount());

        redisUtil.removeZSet(CacheKey.SPOT.getKey() + reservation.getSpotId(), reservationId.toString());
    }

    @Transactional(readOnly = true)
    public ReservationResponse getReservation(Long reservationId) {
        Reservation reservation = reservationReader.findById(reservationId);
        return ReservationResponse.from(reservation);
    }

    @Transactional(readOnly = true)
    public ReservationOrderResponse getWaitingOrder(Long reservationId, Long spotId) {
        Long cacheOrder = redisUtil.getZRank(CacheKey.SPOT.getKey() + spotId, String.valueOf(reservationId));

        if (cacheOrder != null) {
            return ReservationOrderResponse.of((int) (cacheOrder + 1));
        }

        Long order = reservationReader.getReservationBySpotIdAndStatus(spotId, Reservation.Status.WAITING)
                .stream()
                .map(Reservation::getId)
                .filter(i -> i.equals(reservationId))
                .findFirst()
                .orElseThrow(() -> new InvalidReservationStatusException("해당 예약은 대기 상태가 아닙니다."));

        return ReservationOrderResponse.of((int) (order + 1));
    }
}
