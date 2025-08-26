package com.example.waitingreservationservice.repository;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Repository
public class JdbcSpotRepository {
    private final JdbcTemplate jdbcTemplate;

    private static final int BATCH_SIZE = 5_000; // 배치 크기

    @Autowired
    public JdbcSpotRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void bulkInsertSpots(Integer totalRecords) {
        Faker faker = new Faker(new Locale("ko"));

        String insertSQL = "INSERT INTO spots (" +
                "max_capacity, " +
                "remaining_capacity, " +
                "user_id, " +
                "address, " +
                "name, " +
                "status, " +
                "version" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?)";
        int totalBatches = totalRecords / BATCH_SIZE;
        long startTime = System.currentTimeMillis();

        for (int batch = 0; batch < totalBatches; batch++) {
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

            // 배치 삽입
            jdbcTemplate.batchUpdate(insertSQL, new BatchPreparedStatementSetter() {
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

            System.out.printf("Inserted %d/%d records (%.2f%%)%n",
                    (batch + 1) * BATCH_SIZE, totalRecords,
                    ((batch + 1) * BATCH_SIZE * 100.0) / totalRecords);
        }

        long endTime = System.currentTimeMillis();
        System.out.printf("Total time taken: %.2f seconds%n", (endTime - startTime) / 1000.0);
    }
}
