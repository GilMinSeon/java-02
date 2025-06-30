package com.sparta.java_02.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ExecutionTimeAspect {
  // Pointcut: Service 계층의 모든 메서드를 대상으로 지정
  @Pointcut("execution(* com.sparta.java_02.domain..service..*(..))")
  private void allServiceMethods() {
  }

  @Around("allServiceMethods()")
  public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long startTime = System.currentTimeMillis();
    log.info("시작 시간: {}", startTime);

    Object result = joinPoint.proceed();

    long endTime = System.currentTimeMillis();
    long executionTime = endTime - startTime;

    log.info("'{}' 메서드 실행 시간: {}ms", joinPoint.getSignature().toShortString(), executionTime);

    return result;
  }

}
