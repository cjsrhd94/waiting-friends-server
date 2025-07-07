package com.cjsrhd94.waiting_line.global.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RequireNewTransactionAspect {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object proceed(ProceedingJoinPoint pjp) throws Throwable {
        return pjp.proceed();
    }
}
