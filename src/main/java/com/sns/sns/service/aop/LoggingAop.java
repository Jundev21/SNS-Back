package com.sns.sns.service.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@EnableAspectJAutoProxy
public class LoggingAop {

//    @Around("execution(* com.sns.sns.service..*(..)))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        System.out.println("start time " + joinPoint.toString() + " time " + start);
        try{
            return joinPoint.proceed();
        }finally {
            long end = System.currentTimeMillis();
            long time = end - start;
            System.out.println("end " + joinPoint.toString() + " time " + end);
        }
    }
}