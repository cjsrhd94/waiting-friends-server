package com.example.waitingmessage.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "messages")
public class Message {
    @Id
    @Tsid
    private Long id;

    @Column(nullable = false)
    private String receiver;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public Message(
            String receiver,
            String message,
            LocalDateTime createdAt
    ) {
        this.receiver = receiver;
        this.message = message;
        this.createdAt = createdAt;
    }
}
