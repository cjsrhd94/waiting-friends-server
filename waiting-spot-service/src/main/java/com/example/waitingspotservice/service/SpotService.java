package com.example.waitingspotservice.service;

import com.example.waitingspotservice.dto.request.SpotCreateRequest;
import com.example.waitingspotservice.dto.request.SpotRemainingCapacityRequest;
import com.example.waitingspotservice.dto.request.StatusUpdateRequest;
import com.example.waitingspotservice.dto.response.SpotResponse;
import com.example.waitingspotservice.entity.Spot;
import com.example.waitingspotservice.repository.SpotRepository;
import com.example.waitingspotservice.repository.reader.SpotReader;
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
    public Long createSpot(Long userId, SpotCreateRequest request) {
        Spot spot = Spot.builder()
                .name(request.getName())
                .maxCapacity(request.getCapacity())
                .remainingCapacity(request.getCapacity())
                .address(request.getAddress())
                .userId(userId)
                .build();

        return spotRepository.save(spot).getId();
    }

    @Transactional(readOnly = true)
    public SpotResponse getSpot(Long userId, Long spotId) {
        Spot spot = spotReader.findById(spotId);

        return new SpotResponse(
                spot.getId(),
                spot.getName(),
                spot.getAddress(),
                spot.getMaxCapacity(),
                spot.getRemainingCapacity(),
                spot.getStatus().name()
        );
    }

    @Transactional(readOnly = true)
    public Boolean canWait(Long spotId) {
        return spotReader.findById(spotId).canWait();
    }

    @Transactional
    public void decreaseRemainingCapacity(Long spotId, SpotRemainingCapacityRequest request) {
        Spot spot = spotReader.findById(spotId);
        spot.decreaseRemainingCapacity(request.getHeadCount());
        spotRepository.save(spot);
    }

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
    public void decreaseRemainingCapacityWithOptimisticLock(Long spotId, SpotRemainingCapacityRequest request) {
        Spot spot = spotReader.findByIdForOptimistic(spotId);
        spot.decreaseRemainingCapacity(request.getHeadCount());
        spotRepository.save(spot);
    }

    @Transactional
    public void decreaseRemainingCapacityWithPessimisticLock(Long spotId, SpotRemainingCapacityRequest request) {
        Spot spot = spotReader.findByIdForUpdate(spotId);
        spot.decreaseRemainingCapacity(request.getHeadCount());
        spotRepository.save(spot);
    }

    @Transactional
    public void increaseRemainingCapacity(Long spotId, SpotRemainingCapacityRequest request) {
        Spot spot = spotReader.findById(spotId);
        spot.increaseRemainingCapacity(request.getHeadCount());
    }

    @Transactional
    public void updateSpotStatus(Long spotId, StatusUpdateRequest request) {
        Spot spot = spotReader.findById(spotId);
        spot.updateStatus(request.getStatus());
    }
}
