package com.example.waitingreservationservice.service;

import com.example.waitingreservationservice.entity.Spot;
import com.example.waitingreservationservice.repository.ReservationRepository;
import com.example.waitingreservationservice.repository.SpotRepository;
import com.example.waitingreservationservice.repository.reader.SpotReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReservationServiceTest {

    @Autowired
    private SpotRepository spotRepository;

    @Autowired
    private SpotReader spotReader;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private  ReservationService reservationService;

    @AfterEach
    void tearDown() {
        spotRepository.deleteAllInBatch();
        reservationRepository.deleteAllInBatch();
    }

    @Test
    void 레디스_분산락을_활용하여_예약한다() throws InterruptedException {
        Integer remainingCapacity = 1000;
        Spot spot1 = new Spot("점포 1호", 1000, remainingCapacity, "서울특별시 영등포구", 1L);
        spot1.updateStatus("waiting");
        Long spotId = spotRepository.save(spot1).getId();

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    reservationService.reserve(
                            spotId, "010-1234-5678", 1
                    );
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Spot spot = spotReader.findById(spotId);

        assertEquals(remainingCapacity - threadCount, spot.getRemainingCapacity());
    }
}