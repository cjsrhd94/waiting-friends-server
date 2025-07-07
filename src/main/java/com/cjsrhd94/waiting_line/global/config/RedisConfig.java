package com.cjsrhd94.waiting_line.global.config;
import org.springframework.beans.factory.annotation.Value;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.host}")
    private String host;

    private static final String REDISSON_HOST_PREFIX = "redis://";
    private static final String COLON = ":";

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(REDISSON_HOST_PREFIX + host + COLON + port);
        return Redisson.create(config);
    }
}
