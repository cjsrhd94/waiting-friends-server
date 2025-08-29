package com.example.waitingreservationservice.service;

import com.example.waitingreservationservice.entity.Spot;
import com.example.waitingreservationservice.repository.SpotRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class AdminSpotServiceTest {

    private final Faker faker = new Faker(new Locale("ko"));

    private static final Integer INSERT_NUM = 100_000;
    private static final int BATCH_SIZE = 1_000; // 배치 크기
    private static final int THREAD_POOL_SIZE = 4; // CPU 코어 수 기반 스레드 풀

    @Autowired
    private SpotRepository spotRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    @Transactional
    void save_메서드를_사용하여_INSERT한다() {
        Integer capacity = faker.number().numberBetween(100, 1000);

        for (int i = 0; i < INSERT_NUM; i++) {
            Spot spot = Spot.builder()
                    .name(faker.restaurant().name())
                    .maxCapacity(capacity)
                    .remainingCapacity(capacity)
                    .address(faker.address().fullAddress())
                    .userId(1L)
                    .build();

            spotRepository.save(spot);
        }
    }

    @Test
    @Transactional
    void saveAll_메서드를_사용하여_INSERT한다() {
        Integer capacity = faker.number().numberBetween(100, 1000);
        List<Spot> spots = new ArrayList<>();

        for (int i = 0; i < INSERT_NUM; i++) {
            Spot spot = Spot.builder()
                    .name(faker.restaurant().name())
                    .maxCapacity(capacity)
                    .remainingCapacity(capacity)
                    .address(faker.address().fullAddress())
                    .userId(1L)
                    .build();
            spots.add(spot);
        }

        spotRepository.saveAll(spots);
    }

    @Test
    @Transactional
    void jdbc_batch_insert를_사용하여_INSERT한다() throws InterruptedException {
        String sql = "INSERT INTO spots (" +
                "max_capacity, " +
                "remaining_capacity, " +
                "user_id, " +
                "address, " +
                "name, " +
                "status, " +
                "version" +
                ") " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Long maxCapacity = (long) faker.number().numberBetween(100, 1000);
                ps.setLong(1, maxCapacity);
                ps.setLong(2, maxCapacity);
                ps.setLong(3, 1L);
                ps.setString(4, faker.address().fullAddress());
                ps.setString(5, faker.restaurant().name());
                ps.setString(6, "WAITING");
                ps.setLong(7, 1L);
            }

            @Override
            public int getBatchSize() {
                return INSERT_NUM;
            }
        });
    }

    @Test
    @Transactional
    void 멀티_쓰레드와_jdbc_batch_insert를_사용하여_INSERT한다() throws InterruptedException {
        String sql = "INSERT INTO spots (" +
                "max_capacity, " +
                "remaining_capacity, " +
                "user_id, " +
                "address, " +
                "name, " +
                "status, " +
                "version" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?)";
        int totalBatches = INSERT_NUM / BATCH_SIZE;
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        for (int batch = 0; batch < totalBatches; batch++) {
            final int currentBatch = batch;
            executor.submit(() -> {
                List<Object[]> batchList = new ArrayList<>(BATCH_SIZE);

                // 배치 데이터 생성
                for (int i = 0; i < BATCH_SIZE; i++) {
                    Long maxCapacity = (long) faker.number().numberBetween(100, 1000);
                    Long remainingCapacity = maxCapacity;
                    Long userId = 1L;
                    String address = faker.address().fullAddress();
                    String name = faker.restaurant().name();
                    String status = "WAITING";
                    Long version = 1L;

                    batchList.add(new Object[]{
                            maxCapacity, remainingCapacity, userId, address, name, status, version
                    });
                }

                transactionTemplate.execute(status -> {
                            // 배치 삽입
                            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                                @Override
                                public void setValues(PreparedStatement ps, int i) throws SQLException {
                                    Object[] values = batchList.get(i);
                                    ps.setLong(1, (Long) values[0]);
                                    ps.setLong(2, (Long) values[1]);
                                    ps.setLong(3, (Long) values[2]);
                                    ps.setString(4, (String) values[3]);
                                    ps.setString(5, (String) values[4]);
                                    ps.setString(6, (String) values[5]);
                                    ps.setLong(7, (Long) values[6]);
                                }

                                @Override
                                public int getBatchSize() {
                                    return batchList.size();
                                }
                            });
                            return null;
                        }
                );

                System.out.printf("Inserted %d/%d records (%.2f%%)%n",
                        (currentBatch + 1) * BATCH_SIZE, INSERT_NUM,
                        ((currentBatch + 1) * BATCH_SIZE * 100.0) / INSERT_NUM);


            });
        }

        executor.shutdown();
        if (!executor.awaitTermination(1, TimeUnit.HOURS)) {
            System.err.println("Tasks did not finish in time!");
        }
    }
}