package com.example.waitingspotservice.service;

import com.example.waitingspotservice.dto.request.SpotRemainingCapacityRequest;
import com.example.waitingspotservice.entity.Spot;
import com.example.waitingspotservice.repository.SpotRepository;
import com.example.waitingspotservice.repository.reader.SpotReader;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class SpotServiceTest {

    @Autowired
    private SpotService spotService;

    @Autowired
    private SpotReader spotReader;

    @Autowired
    private SpotRepository spotRepository;

    @BeforeEach
    void setUp() {
        Spot spot = new Spot(1L, "점포 1호", 1000, 1000, "서울특별시 영등포구", 1L);
        spotRepository.saveAndFlush(spot);
    }

    @Test
    void reserve() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    spotService.decreaseRemainingCapacityWithPessimisticLock(1L,  new SpotRemainingCapacityRequest(1));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Spot spot = spotReader.findById(1L);

        assertEquals(900, spot.getRemainingCapacity());
    }
}