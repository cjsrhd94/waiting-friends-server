package com.example.waitingmessage.service;

import com.example.waitingmessage.dto.ReservationWaitingRequest;
import com.example.waitingmessage.entity.Message;
import com.example.waitingmessage.entity.Template;
import com.example.waitingmessage.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageConsumer {
    private final MessageRepository messageRepository;

    @KafkaListener(topics = "reservation.message.waiting", containerFactory = "kafkaListenerContainerFactory")
    @RetryableTopic
    @Transactional
    public void sendWaitingMessage(ConsumerRecord<String, ReservationWaitingRequest> record) {
        ReservationWaitingRequest request = record.value();

        String content = MessageFormat.format(
                Template.WAITING.getMessage(),
                request.getSpotName(),
                request.getWaitingNumber(),
                request.getHeadCount(),
                request.getWaitingOrder(),
                request.getWaitingOrder()
        );

        // 메세지 전송 모듈 연동 부분
        try {
            log.info("Sending message to {}: {}", request.getPhoneNumber(), content);
        } catch (Exception e) {
            log.error("Failed to send message to {}: {}", request.getPhoneNumber(), e.getMessage());
        }

        Message message = Message.builder()
                .receiver(request.getPhoneNumber())
                .message(content)
                .createdAt(LocalDateTime.now())
                .build();

        messageRepository.save(message);
    }
}
