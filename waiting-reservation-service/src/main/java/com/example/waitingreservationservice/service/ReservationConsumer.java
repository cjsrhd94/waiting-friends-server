package com.example.waitingreservationservice.service;

import com.example.waitingreservationservice.common.annotation.DistributedLock;
import com.example.waitingreservationservice.common.exception.EnterNotAllowException;
import com.example.waitingreservationservice.common.util.RedisUtil;
import com.example.waitingreservationservice.dto.request.ReservationCreateRequest;
import com.example.waitingreservationservice.entity.Reservation;
import com.example.waitingreservationservice.entity.Spot;
import com.example.waitingreservationservice.repository.ReservationRepository;
import com.example.waitingreservationservice.repository.reader.SpotReader;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.example.waitingreservationservice.common.util.RedisUtil.SPOT_CACHE_KEY;

@Component
@RequiredArgsConstructor
public class ReservationConsumer {
    private final ReservationRepository reservationRepository;
    private final SpotReader spotReader;
    private final RedisUtil redisUtil;

    @KafkaListener(topics = "reservation", containerFactory = "kafkaListenerContainerFactory")
    @DistributedLock(key = "'spot-' + #spotId")
    public Long reserve(ConsumerRecord<String, ReservationCreateRequest> record) {
        ReservationCreateRequest payload = record.value();

        Spot spot = spotReader.findById(payload.getSpotId());

        if ( !spot.canWait() ) {
            throw new EnterNotAllowException("현재 입장할 수 없는 상태입니다.");
        }

        Reservation reservation = new Reservation(payload.getSpotId(), spot.getName(), payload.getPhoneNumber(), payload.getHeadCount());
        Long reservationId = reservationRepository.save(reservation).getId();

        spot.decreaseRemainingCapacity(payload.getHeadCount());

        redisUtil.addZSet(SPOT_CACHE_KEY+ payload.getSpotId(), reservationId.toString(), System.currentTimeMillis());

        return reservationId;
    }
}
