package com.example.waitingreservationservice.repository.reader;


import com.example.waitingreservationservice.common.exception.SpotNotFoundException;
import com.example.waitingreservationservice.entity.Spot;
import com.example.waitingreservationservice.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpotReader {
    private final SpotRepository spotRepository;

    public Spot findById(Long spotId) {
        return spotRepository.findById(spotId)
                .orElseThrow(() -> new SpotNotFoundException(spotId + " : 해당 스팟이 존재하지 않습니다."));
    }

    public Spot findByIdForOptimistic(Long spotId) {
        return spotRepository.findByIdForOptimistic(spotId)
                .orElseThrow(() -> new SpotNotFoundException(spotId + " : 해당 스팟이 존재하지 않습니다."));
    }

    public Spot findByIdForUpdate(Long spotId) {
        return spotRepository.findByIdForUpdate(spotId)
                .orElseThrow(() -> new SpotNotFoundException(spotId + " : 해당 스팟이 존재하지 않습니다."));
    }
}