package com.sparta.java_02.global.aspect;

import com.sparta.java_02.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component // 이 클래스를 스프링 컨테이너로 등록, 스프링에서 실행
public class LoggingAspect {

  // Pointcut: Service 계층의 모든 메서드를 대상으로 지정
  @Pointcut("execution(* com.sparta.java_02.domain..*(..))")
  public void allServiceMethods () {
  // 이거 이해안댐
  }

  // 1. execution: `api` 패키지 내의 모든 메서드 실행 전에 적용
  // 모든 컨트롤러 실행전
  @Before("execution(* com.sparta.java_02.domain..controller..*(..))")
  public void logBeforeApiExecution() {
    log.info("[API-execution] API 메서드 실행 전 로그");
  }

  // 2. within: `domain` 패키지 내의 모든 메서드 실행 전에 적용
  @Before("within(com.sparta.java_02.domain..*)")
  public void logBeforeWithin() {
    log.info("[within] domain 패키지 내부 메서드 실행 전 로그");
  }

  // 3. @annotation: @Loggable 어노테이션이 붙은 메서드 실행 전에만 적용
  @Before("@annotation(com.sparta.java_02.common.annotation.Loggable)")
  public void logBeforeAnnotation() {
    log.info("[@annotation] @Loggable 어노테이션 적용된 메서드 실행 전 로그");
  }

  // 4. JoinPoint 활용: 메서드의 상세 정보 로깅
  //@Before("execution(* com.sparta.java_02.domain..*(..))")
  @Before("allServiceMethods()")
  public void logMethodDetails(JoinPoint joinPoint) {
    log.info("[JoinPoint 활용] 실행된 메서드 이름: {}", joinPoint.getSignature().getName());
    Object[] args = joinPoint.getArgs();
    if (args.length > 0) {
      log.info("전달된 파라미터: {}", args);
    }
  }
  @AfterThrowing(pointcut = "allServiceMethods()", throwing = "exception")
  public void logServiceException (ServiceException exception) {
    // "exception"이라는 이름으로 예외 객체를 받아옴
    log.error("Service Layer Exception: Code = [{}], Message = [{}]",
        exception.getCode(), exception.getMessage());
  }
}
