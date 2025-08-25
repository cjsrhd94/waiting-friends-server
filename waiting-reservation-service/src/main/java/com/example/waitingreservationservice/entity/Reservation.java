package com.example.waitingreservationservice.entity;

import com.example.waitingreservationservice.common.exception.InvalidHeadCountException;
import com.example.waitingreservationservice.common.exception.InvalidPhoneNumberFormatException;
import com.example.waitingreservationservice.common.exception.InvalidReservationStatusException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "head_count", nullable = false)
    private Integer headCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "reservation_date", nullable = false)
    private LocalDateTime reservationDate;

    @Column(name = "spot_id", nullable = false)
    private Long spotId;

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
        this.status = Status.WAITING; // 초기 상태는 대기 중
        this.reservationDate = LocalDateTime.now(); // 예약 날짜는 현재 시간으로 설정
    }

    public enum Status {
        WAITING("WA1", "대기중"),
        CALLING("CA1", "호출 완료"),
        COMPLETED("CO1", "입장 완료"),
        CANCELLED("CA2", "취소");

        private final String code;
        private final String description;

        Status(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public Status findByName(String name) {
            return Arrays.stream(Status.values())
                    .filter(rs -> rs.name().equals(name))
                    .findFirst()
                    .orElseThrow(() -> new InvalidReservationStatusException("유효하지 않은 예약 상태입니다: " + name));
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new InvalidPhoneNumberFormatException("전화번호는 필수 입력 사항입니다.");
        }
        if (!phoneNumber.matches("^0\\d{1,2}-\\d{3,4}-\\d{4}$")) {
            throw new InvalidPhoneNumberFormatException("전화번호는 10자리 또는 11자리 숫자여야 합니다.");
        }
    }

    private void isOverHeadCount(Integer headCount) {
        if (headCount < 1) {
            throw new InvalidHeadCountException("방문 인원은 적어도 1명 이상이어야 합니다.");
        }
    }

    public void updateStatus(String statusName) {
        Status status = this.status.findByName(statusName);

        if (status == this.status) {
            throw new InvalidReservationStatusException("현재 상태와 동일한 상태로 변경할 수 없습니다: " + statusName);
        }

        if (status == Status.WAITING) {
            throw new InvalidReservationStatusException("예약 상태를 WAITING으로 변경할 수 없습니다.");
        }

        this.status = status;
    }

    public Boolean isCancelled() {
        return this.status == Status.CANCELLED;
    }
}
