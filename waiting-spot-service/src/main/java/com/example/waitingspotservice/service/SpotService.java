package com.example.waitingspotservice.service;

import com.example.waitingspotservice.dto.request.SpotCreateRequest;
import com.example.waitingspotservice.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpotService {
    private final SpotRepository spotRepository;

    @Transactional
    public Long createSpot(SpotCreateRequest request) {
        return spotRepository.save(request.toEntity()).getId();
    }
}
