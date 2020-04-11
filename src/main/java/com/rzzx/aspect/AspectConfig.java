package com.rzzx.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
/**
 * ��������
 */
@Aspect
public class AspectConfig {
 
    /**
     * ����֪ͨ
     */
    @Around("execution(* com.rzzx.bean..*(..))")
    public Object testAround(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("before");
        Object obj = joinPoint.proceed();
        System.out.println("after");
        return obj;
    }
}