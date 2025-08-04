package com.example.waitingreservationservice.service;

import com.example.waitingreservationservice.client.SpotFeignClient;
import com.example.waitingreservationservice.dto.request.ReservationCreateRequest;
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

    @KafkaListener(topics = "reservation", containerFactory = "kafkaListenerContainerFactory")
    public Long reserve(ConsumerRecord<String, ReservationCreateRequest> record) {
        ReservationCreateRequest payload = record.value();

        if ( !spotFeignClient.canWaiting(payload.getSpotId()) ) {
            throw new IllegalArgumentException("현재 입장할 수 없는 상태입니다.");
        }

        Reservation reservation = new Reservation(payload.getSpotId(), payload.getPhoneNumber(), payload.getHeadCount());
        return reservationRepository.save(reservation).getId();
    }
}
