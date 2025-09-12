package com.example.waitingreservationservice.common.util;

import com.example.waitingreservationservice.dto.event.ReservationCallingNotificationEvent;
import com.example.waitingreservationservice.dto.event.ReservationWaitingNotificationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendReservationWaiting(ReservationWaitingNotificationEvent event) {
        RawMessage rawMessage = createRawMessage("reservation.waiting", event);
        sendRawMessage("reservation.message", rawMessage);
    }

    public void sendReservationCalling(ReservationCallingNotificationEvent event) {
        RawMessage rawMessage = createRawMessage("reservation.calling", event);
        sendRawMessage("reservation.message", rawMessage);
    }

    private RawMessage createRawMessage(String eventType, Object payload) {
        try {
            return new RawMessage(eventType,objectMapper.writeValueAsString(payload));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Payload JSON 직렬화 실패", e);
        }
    }

    private void sendRawMessage(String topic, RawMessage rawMessage) {
        try {
            String json = objectMapper.writeValueAsString(rawMessage); // RawMessage 전체 직렬화
            kafkaTemplate.send(topic, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("RawMessage JSON 직렬화 실패", e);
        }
    }
}
