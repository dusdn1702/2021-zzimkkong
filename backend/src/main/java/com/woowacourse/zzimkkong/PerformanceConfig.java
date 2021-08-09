package com.woowacourse.zzimkkong;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


@Component
@Aspect
public class PerformanceConfig {
    @Around("@annotation(PerformanceLogging)")
    public Object checkPerformance(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startAt = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long endAt = System.currentTimeMillis();

        String method = proceedingJoinPoint.getSignature().getName();
        System.out.println("====" + method + " 소요 시간 : " + (endAt - startAt));
        return result;
    }
}

