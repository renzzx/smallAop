package com.rzzx.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
/**
 * 切面配置
 */
@Aspect
public class AspectConfig2 {
 
    /**
     * 环绕通知
     */
    @Around("execution(* com.rzzx.bean..*(..))")
    public Object testAround(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("before1");
        Object obj = joinPoint.proceed();
        System.out.println("after1");
        return obj;
    }
}