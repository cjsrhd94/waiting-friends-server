package com.example.waitingreservationservice.service;

import com.example.waitingkafka.service.KafkaProducer;
import com.example.waitingreservationservice.dto.event.ReservationCallingNotificationEvent;
import com.example.waitingreservationservice.dto.event.ReservationCanceledNotificationEvent;
import com.example.waitingreservationservice.dto.event.ReservationWaitingNotificationEvent;
import com.example.waitingreservationservice.dto.request.ReservationCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventProducer {
    private final KafkaProducer kafkaProducer;

    public void produceReservationEvent(ReservationCreateRequest request) {
        kafkaProducer.send("reservation", request);
    }

    public void sendReservationWaiting(ReservationWaitingNotificationEvent event) {
        kafkaProducer.send("reservation.message", "reservation.waiting", event);
    }

    public void sendReservationCalling(ReservationCallingNotificationEvent event) {
        kafkaProducer.send("reservation.message", "reservation.calling", event);
    }

    public void sendReservationCanceled(ReservationCanceledNotificationEvent event) {
        kafkaProducer.send("reservation.message", "reservation.canceled", event);
    }
}
