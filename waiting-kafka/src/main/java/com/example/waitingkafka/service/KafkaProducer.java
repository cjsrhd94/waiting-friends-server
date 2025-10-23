package com.example.waitingkafka.service;

import com.example.waitingkafka.util.JsonConverter;
import com.example.waitingkafka.util.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JsonConverter jsonConverter;

    public void send(String topic, String type, Object payload) {
        kafkaTemplate.send(
                topic,
                jsonConverter.toJson(new Event(type, jsonConverter.toJson(payload)))
        );
    }

    public void send(String topic, Object key, Object payload) {
        kafkaTemplate.send(
                topic,
                key.toString(),
                jsonConverter.toJson(payload)
        );
    }

    public void send(String topic, Object key, String type, Object payload) {
        kafkaTemplate.send(
                topic,
                key.toString(),
                jsonConverter.toJson(new Event(type, jsonConverter.toJson(payload)))
        );
    }

    public void send(String topic, Object payload) {
        kafkaTemplate.send(
                topic,
                jsonConverter.toJson(payload)
        );
    }
}
