package com.example.waitingredis.util;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisUtil {
    private final StringRedisTemplate redisTemplate;

    public RedisUtil(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getData(String key){
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void setData(String key, String value, long expireAt){
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(expireAt);
        valueOperations.set(key,value,expireDuration);
    }

    public void deleteData(String key){
        redisTemplate.delete(key);
    }

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
