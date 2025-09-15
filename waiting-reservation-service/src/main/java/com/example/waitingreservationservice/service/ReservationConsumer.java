package com.example.waitingreservationservice.service;

import com.example.waitingkafka.util.JsonConverter;
import com.example.waitingredis.util.RedisUtil;
import com.example.waitingredis.common.annotation.DistributedLock;
import com.example.waitingreservationservice.common.exception.EnterNotAllowException;
import com.example.waitingreservationservice.common.util.CacheKey;
import com.example.waitingreservationservice.dto.event.ReservationWaitingNotificationEvent;
import com.example.waitingreservationservice.dto.request.ReservationCreateRequest;
import com.example.waitingreservationservice.entity.Reservation;
import com.example.waitingreservationservice.entity.Spot;
import com.example.waitingreservationservice.repository.ReservationRepository;
import com.example.waitingreservationservice.repository.reader.SpotReader;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationConsumer {
    private final ReservationRepository reservationRepository;
    private final SpotReader spotReader;

    private final EventProducer eventProducer;
    private final JsonConverter jsonConverter;
    private final RedisUtil redisUtil;

    @KafkaListener(topics = "reservation", containerFactory = "kafkaListenerContainerFactory")
    @DistributedLock(key = "'spot-' + #spotId")
    public Long reserve(String message) {
        ReservationCreateRequest payload = jsonConverter.fromJson(message, ReservationCreateRequest.class);

        Spot spot = spotReader.findById(payload.getSpotId());

        if ( !spot.canWait() ) {
            throw new EnterNotAllowException("현재 입장할 수 없는 상태입니다.");
        }

        spot.increaseWaitingNumber();

        Reservation reservation = new Reservation(
                payload.getSpotId(),
                spot.getName(),
                payload.getPhoneNumber(),
                payload.getHeadCount(),
                spot.getWaitingNumber()
        );
        Long reservationId = reservationRepository.save(reservation).getId();

        redisUtil.addZSet(CacheKey.SPOT.getKey() + payload.getSpotId(), reservationId.toString(), System.currentTimeMillis());

        eventProducer.sendReservationWaiting(new ReservationWaitingNotificationEvent(reservation));

        return reservationId;
    }
}
