package com.cjsrhd94.waiting_line.domain.reservation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "spot_id", nullable = false)
    private Long spotId;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "head_count", nullable = false)
    private Integer headCount;

    @Column(name = "status", nullable = false)
    private ReservationStatus status;

    @Column(name = "reservation_date", nullable = false)
    private LocalDateTime reservationDate;

    public Reservation(
            Long spotId,
            String phoneNumber,
            Integer headCount
    ) {
        isOverHeadCount(headCount);

        this.id = UUID.randomUUID();
        this.spotId = spotId;
        this.phoneNumber = phoneNumber;
        this.headCount = headCount;
        this.status = ReservationStatus.WAITING; // 초기 상태는 대기 중
        this.reservationDate = LocalDateTime.now(); // 예약 날짜는 현재 시간으로 설정
    }

    private enum ReservationStatus {
        WAITING, // 대기 중
        CALLING, // 호출 중
        COMPLETED, // 입장 완료
        CANCELLED // 취소됨
    }

    private void isOverHeadCount(Integer headCount) {
        if (headCount < 1) {
            throw new IllegalArgumentException("방문 인원은 적어도 1명 이상이어야 합니다.");
        }
    }
}
