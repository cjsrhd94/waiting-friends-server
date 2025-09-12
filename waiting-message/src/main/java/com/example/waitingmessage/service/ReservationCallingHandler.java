package com.example.waitingmessage.service;

import com.example.waitingmessage.entity.Message;
import com.example.waitingmessage.entity.Template;
import com.example.waitingmessage.event.ReservationCallingNotificationEvent;
import com.example.waitingmessage.repository.MessageRepository;
import com.example.waitingmessage.util.TypedEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationCallingHandler implements TypedEventHandler<ReservationCallingNotificationEvent> {
    private final MessageRepository messageRepository;

    @Override
    public String getEventType() {
        return "reservation.calling";
    }

    @Override
    public Class<ReservationCallingNotificationEvent> getDtoClass() {
        return ReservationCallingNotificationEvent.class;
    }

    @Override
    public void handle(ReservationCallingNotificationEvent event) {
        String content = MessageFormat.format(
                Template.CALLING.getMessage(),
                event.getSpotName(),
                event.getWaitingNumber()
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
