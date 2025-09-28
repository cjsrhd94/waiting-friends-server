package com.example.waitingredis.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PERCache {

    /**
     * 캐시 키 (SpEL 지원)
     */
    String key();

    /**
     * 캐시 TTL
     */
    long ttl() default 300;

    /**
     * PER 베타 계수
     */
    double beta() default 1.0;

}
