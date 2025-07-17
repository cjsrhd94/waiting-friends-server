package com.example.waitingspotservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "spots")
public class Spot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    private enum Status {
        ALLOW,      // 바로 입장
        WAITING,    // 대기 모드
        NOT_ALLOW,  // 입장 불가
        CLOSED,     // 운영 종료
    }

    @Builder
    public Spot(
            Long id,
            String name,
            Integer capacity
    ) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.status = Status.CLOSED;
    }

    public boolean canWaiting() {
        return this.status == Status.ALLOW || this.status == Status.WAITING;
    }
}
