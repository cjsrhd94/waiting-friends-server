package com.example.waitingspotservice.repository;

import com.example.waitingspotservice.entity.Spot;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpotRepository extends JpaRepository<Spot, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT s FROM Spot s WHERE s.id = :spotId")
    Optional<Spot> findByIdForOptimistic(Long spotId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "1000")})
    @Query("SELECT s FROM Spot s WHERE s.id = :spotId")
    Optional<Spot> findByIdForUpdate(Long spotId);
}