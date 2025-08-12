package com.example.waitingreservationservice.service;

import com.example.waitingreservationservice.client.SpotFeignClient;
import com.example.waitingreservationservice.common.annotation.DistributedLock;
import com.example.waitingreservationservice.common.util.RedisUtil;
import com.example.waitingreservationservice.dto.request.ReservationCreateRequest;
import com.example.waitingreservationservice.dto.request.SpotRemainingCapacityRequest;
import com.example.waitingreservationservice.dto.response.SpotRemainingCapacityResponse;
import com.example.waitingreservationservice.entity.Reservation;
import com.example.waitingreservationservice.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationConsumer {
    private final SpotFeignClient spotFeignClient;
    private final ReservationRepository reservationRepository;
    private final RedisUtil redisUtil;

    @KafkaListener(topics = "reservation", containerFactory = "kafkaListenerContainerFactory")
    @DistributedLock(key = "#spotId")
    public Long reserve(ConsumerRecord<String, ReservationCreateRequest> record) {
        ReservationCreateRequest payload = record.value();

        if ( !spotFeignClient.canWaiting(payload.getSpotId()) ) {
            throw new IllegalArgumentException("현재 입장할 수 없는 상태입니다.");
        }

        Reservation reservation = new Reservation(payload.getSpotId(), payload.getPhoneNumber(), payload.getHeadCount());
        Long reservationId = reservationRepository.save(reservation).getId();

        spotFeignClient.decreaseRemainingCapacity(payload.getSpotId(), new SpotRemainingCapacityRequest(payload.getHeadCount()));

        redisUtil.rPush("spot:"+ payload.getSpotId(), reservationId.toString());

        return reservationId;
    }
}
