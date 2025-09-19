package com.example.waitingreservationservice.entity;

import com.example.waitingreservationservice.common.exception.InvalidSpotStatusException;
import io.hypersistence.utils.hibernate.id.Tsid;
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
    @Tsid
    private Long id;

    @Version
    private Long version;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "waiting_number", nullable = false)
    private Integer waitingNumber;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    public enum Status {
        ALLOW,      // 바로 입장
        WAITING,    // 대기 모드
        NOT_ALLOW,  // 입장 불가
        CLOSED;     // 운영 종료

        public Status findByName(String name) {
            return Arrays.stream(Status.values())
                    .filter(s -> s.name().equalsIgnoreCase(name))
                    .findFirst()
                    .orElseThrow(() -> new InvalidSpotStatusException("유효하지 않은 영업 상태입니다: " + name));
        }
    }

    @Builder
    public Spot(
            String name,
            String address,
            Long userId
    ) {
        this.name = name;
        this.waitingNumber = 0;
        this.address = address;
        this.status = Status.CLOSED;
        this.userId = userId;
    }

    public boolean canWait() {
        return this.status == Status.WAITING;
    }

    public void increaseWaitingNumber() {
        this.waitingNumber++;
    }

    public boolean isOwner(Long userId) {
        return this.userId.equals(userId);
    }

    public void updateStatus(String status) {
        this.status = this.status.findByName(status);

        // 영업 종료시 대기 번호를 0으로 초기화 한다.
        if (this.status == Status.CLOSED) {
            this.waitingNumber = 0;
        }
    }
}
