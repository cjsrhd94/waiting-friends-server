package com.cjsrhd94.waiting_line.domain.reservation.repository;

import com.cjsrhd94.waiting_line.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
