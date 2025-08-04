package com.example.waitingreservationservice.service;

import com.example.waitingreservationservice.dto.request.ReservationCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReservationProducer {
    private final KafkaTemplate<String, ReservationCreateRequest> kafkaTemplate;

    public void produceReservationEvent(ReservationCreateRequest request) {
        kafkaTemplate.send("reservation", request);
    }
}
