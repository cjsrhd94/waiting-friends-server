package com.example.waitingreservationservice.service;

import com.example.waitingreservationservice.client.SpotFeignClient;
import com.example.waitingreservationservice.common.annotation.DistributedLock;
import com.example.waitingreservationservice.common.util.RedisUtil;
import com.example.waitingreservationservice.dto.request.ReservationUpdateRequest;
import com.example.waitingreservationservice.dto.response.ReservationResponse;
import com.example.waitingreservationservice.entity.Reservation;
import com.example.waitingreservationservice.repository.ReservationRepository;

import com.example.waitingreservationservice.repository.reader.ReservationReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationReader reservationReader;
    private final SpotFeignClient spotFeignClient;
    private final RedisUtil redisUtil;

    private final static String SPOT_CACHE_KEY = "spot:";

    @Transactional
    @DistributedLock(key = "#spotId")
    public Long reserve(
            Long spotId,
            String phoneNumber,
            Integer headCount
    ) {
        if (!spotFeignClient.canWaiting(spotId)) {
            throw new IllegalArgumentException("현재 입장할 수 없는 상태입니다.");
        }

        Reservation reservation = new Reservation(spotId, phoneNumber, headCount);
        return reservationRepository.save(reservation).getId();
    }

    @Transactional
    public void updateReservationStatus(
            Long reservationId,
            ReservationUpdateRequest request
    ) {
        Reservation reservation = reservationReader.findById(reservationId);
        reservation.updateStatus(request.getStatus());

        redisUtil.remove(SPOT_CACHE_KEY, 0, reservation.toString());
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
        List<String> waitingIdsInRedis = redisUtil.getList(SPOT_CACHE_KEY + spotId, 0, -1);
        for (int i = 0; i < waitingIdsInRedis.size(); i++) {
            if (waitingIdsInRedis.get(i).equals(String.valueOf(reservationId))) {
                return (long) (i + 1);
            }
        }

        List<Long> waitingIds = reservationReader.getReservationBySpotIdAndStatus(
                        spotId,
                        Reservation.ReservationStatus.WAITING
                ).stream()
                .map(Reservation::getId)
                .collect(Collectors.toList());

        for (int i = 0; i < waitingIds.size(); i++) {
            if (waitingIds.get(i).equals(reservationId)) {
                return (long) (i + 1);
            }
        }

        throw new IllegalArgumentException("해당 예약이 대기 중인 상태가 아닙니다.");
    }
}
