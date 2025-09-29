package com.example.waitinguserservice.idgenerator;

import com.github.f4b6a3.tsid.TsidCreator;
import com.github.f4b6a3.ulid.UlidCreator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootTest
public class IdGenerationConcurrentBenchmarkTest {

    private static final int THREAD_COUNT = 10;
    private static final int ITERATIONS_PER_THREAD = 100_000;
    private static final int TOTAL_ITERATIONS = THREAD_COUNT * ITERATIONS_PER_THREAD;

    @Test
    public void compareConcurrentIdGenerationPerformance() throws InterruptedException {
        System.out.println("=== 멀티스레드 ID 생성 속도 비교 테스트 ===");
        System.out.printf("스레드 수: %d, 스레드당 반복: %,d, 총 반복: %,d%n",
                THREAD_COUNT, ITERATIONS_PER_THREAD, TOTAL_ITERATIONS);
        System.out.println();

        testConcurrentAutoIncrement();
        testConcurrentUuidV4();
        testConcurrentUuidV7();
        testConcurrentUlid();
        testConcurrentTsid();

        System.out.println("=== 멀티스레드 테스트 완료 ===");
    }

    private void testConcurrentAutoIncrement() throws InterruptedException {
        AtomicLong counter = new AtomicLong(0);
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        long startTime = System.nanoTime();

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < ITERATIONS_PER_THREAD; j++) {
                        counter.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        long endTime = System.nanoTime();
        executor.shutdown();

        printResults("Auto Increment", startTime, endTime);
    }

    private void testConcurrentUuidV4() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        long startTime = System.nanoTime();

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < ITERATIONS_PER_THREAD; j++) {
                        UUID.randomUUID();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        long endTime = System.nanoTime();
        executor.shutdown();

        printResults("UUID v4", startTime, endTime);
    }

    private void testConcurrentUuidV7() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        long startTime = System.nanoTime();

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < ITERATIONS_PER_THREAD; j++) {
                        UuidV7Generator.generate();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        long endTime = System.nanoTime();
        executor.shutdown();

        printResults("UUID v7", startTime, endTime);
    }

    private void testConcurrentUlid() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        long startTime = System.nanoTime();

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < ITERATIONS_PER_THREAD; j++) {
                        UlidCreator.getUlid();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        long endTime = System.nanoTime();
        executor.shutdown();

        printResults("ULID", startTime, endTime);
    }

    private void testConcurrentTsid() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        long startTime = System.nanoTime();

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < ITERATIONS_PER_THREAD; j++) {
                        TsidCreator.getTsid();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        long endTime = System.nanoTime();
        executor.shutdown();

        printResults("TSID", startTime, endTime);
    }

    private void printResults(String testName, long startTime, long endTime) {
        long durationNanos = endTime - startTime;
        double durationMillis = durationNanos / 1_000_000.0;
        long idsPerSecond = (long) (TOTAL_ITERATIONS / (durationNanos / 1_000_000_000.0));

        System.out.printf("%-15s: %.2f ms, %,d ids/sec%n",
                testName, durationMillis, idsPerSecond);
    }
}

