package com.example.waitingmessage.service;

import com.example.waitingmessage.event.RawMessage;
import com.example.waitingmessage.util.EventDispatcher;
import com.example.waitingmessage.util.JsonConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageConsumer {
    private final JsonConverter jsonConverter;
    private final EventDispatcher eventDispatcher;

    @KafkaListener(topics = "reservation.message", containerFactory = "kafkaListenerContainerFactory")
    @RetryableTopic
    @Transactional
    public void consume(String message) {
        eventDispatcher.dispatch(
                jsonConverter.fromJson(message, RawMessage.class)
        );
    }
}
