package com.example.waitingreservationservice.service;

import com.example.waitingreservationservice.dto.request.ReservationCreateRequest;
import com.example.waitingreservationservice.dto.request.ReservationWaitingRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReservationProducer {
    private final KafkaTemplate<String, ReservationCreateRequest> kafkaTemplate;
    private final KafkaTemplate<String, ReservationWaitingRequest> waitingKafkaTemplate;

    public void produceReservationEvent(ReservationCreateRequest request) {
        kafkaTemplate.send("reservation", request);
    }

    public void produceWaitingEvent(ReservationWaitingRequest request) {
        waitingKafkaTemplate.send("reservation.message.waiting", request);
    }
}
