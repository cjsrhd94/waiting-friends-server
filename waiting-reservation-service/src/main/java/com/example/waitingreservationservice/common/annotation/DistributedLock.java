package com.example.waitingreservationservice.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    // 분산 락 key
    String key();

    // 시간 단위
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    // 대기 시간
    long waitTime() default 10L;

    // 소유 시간
    long leaseTime() default 5L;
}
