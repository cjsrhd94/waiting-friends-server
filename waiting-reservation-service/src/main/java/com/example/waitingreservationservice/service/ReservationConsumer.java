package com.example.waitingreservationservice.service;

import com.example.waitingkafka.util.JsonConverter;
import com.example.waitingreservationservice.dto.request.ReservationCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationConsumer {
    private final ReservationService reservationService;
    private final JsonConverter jsonConverter;

    @KafkaListener(topics = "reservation", containerFactory = "kafkaListenerContainerFactory")
    public Long reserve(String message) {
        ReservationCreateRequest payload = jsonConverter.fromJson(message, ReservationCreateRequest.class);
        return reservationService.reserve(payload.getSpotId(), payload.getPhoneNumber(), payload.getHeadCount());
    }
}
