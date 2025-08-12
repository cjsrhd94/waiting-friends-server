package com.example.waitingspotservice.service;

import com.example.waitingspotservice.dto.request.SpotCreateRequest;
import com.example.waitingspotservice.dto.request.SpotRemainingCapacityRequest;
import com.example.waitingspotservice.dto.response.SpotRemainingCapacityResponse;
import com.example.waitingspotservice.dto.response.SpotResponse;
import com.example.waitingspotservice.entity.Spot;
import com.example.waitingspotservice.repository.SpotRepository;
import com.example.waitingspotservice.repository.reader.SpotReader;
import lombok.RequiredArgsConstructor;
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
                .userId(userId)
                .build();
        return spotRepository.save(spot).getId();
    }

    @Transactional(readOnly = true)
    public SpotResponse getSpot(Long userId, Long spotId) {
        Spot spot = spotReader.findById(spotId);

        if (!spot.getUserId().equals(userId)) {
            throw new IllegalArgumentException("해당 스팟에 대한 권한이 없습니다.");
        }

        return new SpotResponse(
                spot.getId(),
                spot.getName(),
                spot.getMaxCapacity(),
                spot.getRemainingCapacity(),
                spot.getStatus().name()
        );
    }

    @Transactional(readOnly = true)
    public Boolean canWaiting(Long spotId) {
        return spotReader.findById(spotId).canWaiting();
    }

    @Transactional
    public SpotRemainingCapacityResponse decreaseRemainingCapacity(Long spotId, SpotRemainingCapacityRequest request) {
        Spot spot = spotReader.findById(spotId);

        if (spot.getRemainingCapacity() < request.getHeadCount()) {
            throw new IllegalArgumentException("남은 수용 인원이 부족합니다.");
        }

        Integer remainingCapacity = spot.decreaseRemainingCapacity(request.getHeadCount());
        return new SpotRemainingCapacityResponse(remainingCapacity);
    }
}
