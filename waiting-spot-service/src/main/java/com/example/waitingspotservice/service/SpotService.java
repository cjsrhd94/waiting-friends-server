package com.example.waitingspotservice.service;

import com.example.waitingspotservice.client.UserFeignClient;
import com.example.waitingspotservice.dto.request.SpotCreateRequest;
import com.example.waitingspotservice.dto.response.SpotResponse;
import com.example.waitingspotservice.dto.response.UserResponse;
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
    private final UserFeignClient userFeignClient;

    @Transactional
    public Long createSpot(SpotCreateRequest request) {
        return spotRepository.save(request.toEntity()).getId();
    }

    @Transactional(readOnly = true)
    public SpotResponse getSpot(String email, Long spotId) {
        UserResponse response = userFeignClient.getUser(email);
        Spot spot = spotReader.findById(spotId);

        if (!spot.getUserId().equals(response.getUserId())) {
            throw new IllegalArgumentException("해당 스팟에 대한 권한이 없습니다.");
        }

        return new SpotResponse(
                spot.getId(),
                spot.getName(),
                spot.getCapacity(),
                spot.getStatusToString()
        );
    }

    @Transactional(readOnly = true)
    public Boolean canWaiting(Long spotId) {
        return spotReader.findById(spotId).canWaiting();
    }
}
