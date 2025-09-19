package com.example.waitingreservationservice.service;

import com.example.waitingreservationservice.common.exception.SpotAccessDeniedException;
import com.example.waitingreservationservice.dto.request.SpotCreateRequest;
import com.example.waitingreservationservice.dto.request.SpotStatusUpdateRequest;
import com.example.waitingreservationservice.dto.response.SpotResponse;
import com.example.waitingreservationservice.entity.Spot;
import com.example.waitingreservationservice.repository.SpotRepository;
import com.example.waitingreservationservice.repository.reader.SpotReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminSpotService {
    private final SpotRepository spotRepository;
    private final SpotReader spotReader;


    @Transactional
    public Long createSpot(Long userId, SpotCreateRequest request) {
        Spot spot = Spot.builder()
                .name(request.getName())
                .address(request.getAddress())
                .userId(userId)
                .build();

        return spotRepository.save(spot).getId();
    }

    @Transactional
    public void updateSpotStatus(Long userId, Long spotId, SpotStatusUpdateRequest request) {
        Spot spot = spotReader.findById(spotId);
        validateOwner(userId, spot);

        spot.updateStatus(request.getStatus());
    }

    @Transactional(readOnly = true)
    public SpotResponse getSpot(Long userId, Long spotId) {
        Spot spot = spotReader.findById(spotId);
        validateOwner(userId, spot);

        return SpotResponse.from(spot);
    }

    @Transactional(readOnly = true)
    public List<SpotResponse> getSpotsBySearch(String address) {
        return spotReader.findSpotsBySearch(address).stream()
                .map(SpotResponse::from)
                .toList();
    }

    private void validateOwner(Long userId, Spot spot) {
        if (!spot.isOwner(userId) ) {
            throw new SpotAccessDeniedException();
        }
    }
}
