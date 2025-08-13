package com.example.waitingspotservice.entity;

import com.example.waitingspotservice.common.exception.NotEnoughCapacityException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

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

    @Column(name = "address", nullable = false)
    private String address;

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

        public Status findByName(String name) {
            return Arrays.stream(Status.values())
                    .filter(s -> s.name().equals(name))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 영업 상태입니다: " + name));
        }
    }

    @Builder
    public Spot(
            Long id,
            String name,
            Integer maxCapacity,
            Integer remainingCapacity,
            String address,
            Long userId
    ) {
        this.id = id;
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.remainingCapacity = remainingCapacity;
        this.address = address;
        this.status = Status.CLOSED;
        this.userId = userId;
    }

    public boolean canWait() {
        return this.status == Status.WAITING;
    }

    public void decreaseRemainingCapacity(Integer headCount) {
        if (this.remainingCapacity < headCount) {
            throw new NotEnoughCapacityException(
                    "최대 " + remainingCapacity + "명까지 대기할 수 있습니다. 요청한 인원: " + headCount
            );
        }

        this.remainingCapacity = this.remainingCapacity - headCount;
    }

    public void updateStatus(String status) {
        this.status = this.status.findByName(status);

        // 영업 종료시 잔여 수용량을 최대 수용량으로 초기화 한다.
        if (this.status == Status.CLOSED) {
            this.remainingCapacity = this.maxCapacity;
        }
    }
}
