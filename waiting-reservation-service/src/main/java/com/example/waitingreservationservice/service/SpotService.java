package com.example.waitingreservationservice.service;

import com.example.waitingreservationservice.dto.request.SpotCreateRequest;
import com.example.waitingreservationservice.dto.request.SpotRemainingCapacityRequest;
import com.example.waitingreservationservice.dto.request.StatusUpdateRequest;
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
    public void updateSpotStatus(Long spotId, StatusUpdateRequest request) {
        Spot spot = spotReader.findById(spotId);
        spot.updateStatus(request.getStatus());
    }
}
