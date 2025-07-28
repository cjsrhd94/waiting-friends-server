package com.example.waitingreservationservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "spot_id", nullable = false)
    private Long spotId;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "head_count", nullable = false)
    private Integer headCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReservationStatus status;

    @Column(name = "reservation_date", nullable = false)
    private LocalDateTime reservationDate;

    public Reservation(
            Long spotId,
            String phoneNumber,
            Integer headCount
    ) {
        validatePhoneNumber(phoneNumber);
        isOverHeadCount(headCount);

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

    public String getStatusToString() {
        return status.name();
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("전화번호는 필수 입력 사항입니다.");
        }
        if (!phoneNumber.matches("^0\\d{1,2}-\\d{3,4}-\\d{4}$")) {
            throw new IllegalArgumentException("전화번호는 10자리 또는 11자리 숫자여야 합니다.");
        }
    }

    private void isOverHeadCount(Integer headCount) {
        if (headCount < 1) {
            throw new IllegalArgumentException("방문 인원은 적어도 1명 이상이어야 합니다.");
        }
    }
}
