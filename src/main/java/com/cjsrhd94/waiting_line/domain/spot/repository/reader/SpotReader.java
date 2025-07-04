package com.cjsrhd94.waiting_line.domain.spot.repository.reader;

import com.cjsrhd94.waiting_line.domain.spot.entity.Spot;
import com.cjsrhd94.waiting_line.domain.spot.repository.SpotRepository;
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
                .orElseThrow(() -> new IllegalArgumentException(spotId + "번 스팟이 존재하지 않습니다."));
    }

    public boolean existsById(Long spotId) {
        return spotRepository.existsById(spotId);

    }
}