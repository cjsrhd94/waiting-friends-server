package com.cjsrhd94.waiting_line.domain.spot.repository;

import com.cjsrhd94.waiting_line.domain.spot.entity.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotRepository extends JpaRepository<Spot, Long> {
}