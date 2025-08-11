package com.example.waitingreservationservice.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisUtil {

    private final StringRedisTemplate redisTemplate;

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

    public List<String> getList(String key, long start, long end){
        return redisTemplate.opsForList().range(key, start, end);
    }

    public void rPush(String key, String value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    public void remove(String key, long count, String value) {
        redisTemplate.opsForList().remove(key, count, value);
    }
}
