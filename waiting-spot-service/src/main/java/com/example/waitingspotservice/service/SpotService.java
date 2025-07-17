package com.example.waitingspotservice.service;

import com.example.waitingspotservice.dto.request.SpotCreateRequest;
import com.example.waitingspotservice.repository.SpotRepository;
import com.example.waitingspotservice.repository.reader.SpotReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpotService {
    private final SpotRepository spotRepository;
    private final SpotReader spotReader;

    @Transactional
    public Long createSpot(SpotCreateRequest request) {
        return spotRepository.save(request.toEntity()).getId();
    }

    public Boolean canWaiting(Long spotId) {
        return spotReader.findById(spotId).canWaiting();
    }
}
