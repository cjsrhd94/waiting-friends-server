package com.example.waitingredis.common.aop;

import com.example.waitingredis.common.annotation.PERCache;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class PERCacheAspect {
    private static final String PER_CACHE_KEY_PREFIX = "PER:";
    private static final String DELTA_SUFFIX = ":DELTA";
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final DefaultRedisScript<List> getScript;
    private final DefaultRedisScript<Long> setScript;

    @Around("@annotation(com.example.waitingredis.common.annotation.PERCache)")
    public Object cache(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        PERCache perCache = method.getAnnotation(PERCache.class);

        String key = PER_CACHE_KEY_PREFIX + CustomSpringELParser.getDynamicValue(
                signature.getParameterNames(),
                joinPoint.getArgs(),
                perCache.key()
        );
        String deltaKey = key + DELTA_SUFFIX;

        List<Object> result = (List<Object>) redisTemplate.execute(getScript, List.of(key, deltaKey));
        List<String> valueList = (List<String>) result.get(0);

        if (valueList == null || valueList.size() < 2) {
            valueList = List.of(null, null);
        }

        String data = valueList.get(0);
        String deltaStr = valueList.get(1);

        if (data == null || deltaStr == null
                        || - Long.parseLong(deltaStr) * perCache.beta() * Math.log(new Random().nextDouble()) >= (long) result.get(1)
        ) {
            long start = System.currentTimeMillis();
            Object computed = joinPoint.proceed();
            long computationTime = System.currentTimeMillis() - start;

            redisTemplate.execute(
                    setScript,
                    List.of(key, deltaKey),
                    objectMapper.writeValueAsString(computed),
                    String.valueOf(computationTime),
                    String.valueOf(perCache.ttl())
            );

            return computed;
        }

        Class<?> clazz = method.getReturnType();
        return objectMapper.readValue(data, clazz);
    }
}
