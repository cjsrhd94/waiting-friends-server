package com.example.waitingreservationservice.service;


import com.example.waitingreservationservice.entity.Spot;
import com.example.waitingreservationservice.repository.SpotRepository;
import com.example.waitingreservationservice.repository.reader.SpotReader;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
class SpotServiceTest {

    @Autowired
    private SpotService spotService;

    @Autowired
    private SpotReader spotReader;

    @Autowired
    private SpotRepository spotRepository;

    @AfterEach
    void tearDown() {
        spotRepository.deleteAll();
    }

    @Test
    void 낙관적_락을_사용하여_잔여_수용량을_감소시킨다() throws InterruptedException {
        Integer remainingCapacity = 1000;
        Spot spot1 = new Spot("점포 1호", 1000, remainingCapacity, "서울특별시 영등포구", 1L);
        Long spotId = spotRepository.save(spot1).getId();

        int threadCount = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    spotService.decreaseRemainingCapacityWithOptimisticLock(
                            spotId, 1
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

    @Test
    void 비관적_락을_사용하여_잔여_수용량을_감소시킨다() throws InterruptedException {
        Integer remainingCapacity = 1000;
        Spot spot1 = new Spot("점포 1호", 1000, remainingCapacity, "서울특별시 영등포구", 1L);
        Long spotId = spotRepository.save(spot1).getId();

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    spotService.decreaseRemainingCapacityWithPessimisticLock(
                            spotId, 1
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