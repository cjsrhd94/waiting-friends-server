package com.example.waitingmessage.service;

import com.example.waitingkafka.util.TypedEventHandler;
import com.example.waitingmessage.entity.Message;
import com.example.waitingmessage.entity.Template;
import com.example.waitingmessage.event.ReservationCanceledNotificationEvent;
import com.example.waitingmessage.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationCanceledHandler implements TypedEventHandler<ReservationCanceledNotificationEvent> {
    private final MessageRepository messageRepository;

    @Override
    public String eventType() {
        return "reservation.canceled";
    }

    @Override
    public Class<ReservationCanceledNotificationEvent> getDtoClass() {
        return ReservationCanceledNotificationEvent.class;
    }

    @Override
    public void handle(ReservationCanceledNotificationEvent event) {
        String content = MessageFormat.format(
                Template.CANCELED.getMessage(),
                event.getSpotName()
        );

        // 메세지 전송 모듈 연동 부분
        try {
            log.info("Sending message to {}: {}", event.getPhoneNumber(), content);
        } catch (Exception e) {
            log.error("Failed to send message to {}: {}", event.getPhoneNumber(), e.getMessage());
        }

        Message message = Message.builder()
                .receiver(event.getPhoneNumber())
                .message(content)
                .createdAt(LocalDateTime.now())
                .build();

        messageRepository.save(message);
    }
}
