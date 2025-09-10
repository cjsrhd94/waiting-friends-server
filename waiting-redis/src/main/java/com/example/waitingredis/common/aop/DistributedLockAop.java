package com.example.waitingredis.common.aop;

import com.example.waitingredis.common.annotation.DistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Order(1)
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAop {
    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;
    private final RequireNewTransactionAspect requireNewTransactionAspect;

    @Around("@annotation(com.example.waitingredis.common.annotation.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 메소드에 @DistributedLock 어노테이션이 있는지 확인
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        // 분산락 키 생성
        String key = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(
                signature.getParameterNames(),
                joinPoint.getArgs(),
                distributedLock.key()
        );

        // 분산락 시도
        RLock rLock = redissonClient.getLock(key);
        try {
            boolean available = rLock.tryLock(
                    distributedLock.waitTime(),
                    distributedLock.leaseTime(),
                    distributedLock.timeUnit()
            );

            if (!available) {
                log.error("lock not available [method:{}] [key:{}]", method, key);
                return false;
            }

            return requireNewTransactionAspect.proceed(joinPoint);
        } catch (InterruptedException e) {
            log.error("lock interrupted [method:{}] [key:{}]", method, key);
            throw new InterruptedException();
        } finally {
            try {
                rLock.unlock();
            } catch (IllegalMonitorStateException e) {
                log.info("Redisson Lock Already UnLock [method:{}] [key:{}]", method, key);
            }
        }
    }
}
