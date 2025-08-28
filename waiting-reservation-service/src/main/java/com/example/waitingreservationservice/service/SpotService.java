package com.example.waitingreservationservice.service;

import com.example.waitingreservationservice.dto.response.SpotResponse;
import com.example.waitingreservationservice.entity.Spot;
import com.example.waitingreservationservice.repository.SpotRepository;
import com.example.waitingreservationservice.repository.reader.SpotReader;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.hibernate.StaleObjectStateException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SpotService {
    private final SpotRepository spotRepository;
    private final SpotReader spotReader;

    @Transactional
    @Retryable(
            retryFor = {
                    ObjectOptimisticLockingFailureException.class,
                    OptimisticLockException.class,
                    StaleObjectStateException.class
            },
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000)
    )
    public void decreaseRemainingCapacityWithOptimisticLock(Long spotId, Integer headCount) {
        Spot spot = spotReader.findByIdForOptimistic(spotId);
        spot.decreaseRemainingCapacity(headCount);
        spotRepository.save(spot);
    }

    @Transactional
    public void decreaseRemainingCapacityWithPessimisticLock(Long spotId, Integer headCount) {
        Spot spot = spotReader.findByIdForUpdate(spotId);
        spot.decreaseRemainingCapacity(headCount);
        spotRepository.save(spot);
    }

    @Transactional(readOnly = true)
    public SpotResponse getSpot(Long spotId) {
        Spot spot = spotReader.findById(spotId);

        return SpotResponse.from(spot);
    }
}
