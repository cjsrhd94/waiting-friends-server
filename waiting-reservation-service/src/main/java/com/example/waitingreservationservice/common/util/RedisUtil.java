package com.example.waitingreservationservice.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisUtil {

    private final StringRedisTemplate redisTemplate;

    public Long getZRank(String key, String value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }

    public void addZSet(String key, String value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    public void removeZSet(String key, String value) {
        redisTemplate.opsForZSet().remove(key, value);
    }
}
