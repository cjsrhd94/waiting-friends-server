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

    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity;

    @Column(name = "remaining_capacity", nullable = false)
    private Integer remainingCapacity;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    public enum Status {
        ALLOW,      // 바로 입장
        WAITING,    // 대기 모드
        NOT_ALLOW,  // 입장 불가
        CLOSED;     // 운영 종료
    }

    @Builder
    public Spot(
            Long id,
            String name,
            Integer maxCapacity,
            Integer remainingCapacity,
            Long userId
    ) {
        this.id = id;
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.remainingCapacity = remainingCapacity;
        this.status = Status.CLOSED;
        this.userId = userId;
    }

    public boolean canWaiting() {
        return this.status == Status.WAITING;
    }

    public Integer decreaseRemainingCapacity(Integer headCount) {
        this.remainingCapacity = this.remainingCapacity - headCount;
        return this.remainingCapacity;
    }
}
