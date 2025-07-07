package com.sparta.java_02.global.exception;

import com.sparta.java_02.common.exception.ServiceException;
import com.sparta.java_02.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Hidden;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 스프링한테 여기는 글로벌한 여기는 뭔가 핸들링할 수 있는 뭔가가 있어라고 알려줌
@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

  private final String VALIDATE_ERROR = "VALIDATE_ERROR";
  private final String SERVER_ERROR = "SERVER_ERROR";

  @ExceptionHandler(ServiceException.class)
  public ResponseEntity<?> handleResponseException(ServiceException ex) {
    return ApiResponse.error(ex.getCode(), ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
    AtomicReference<String> errors = new AtomicReference<>("");
    ex.getBindingResult().getAllErrors().forEach(c -> errors.set(c.getDefaultMessage()));

    return ApiResponse.badRequest(VALIDATE_ERROR, String.valueOf(errors));
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<?> bindException(BindException ex) {
    AtomicReference<String> errors = new AtomicReference<>("");
    ex.getBindingResult().getAllErrors().forEach(c -> errors.set(c.getDefaultMessage()));

    return ApiResponse.badRequest(VALIDATE_ERROR, String.valueOf(errors));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> serverException(Exception ex) {
    return ApiResponse.serverError(SERVER_ERROR, ex.getMessage());
  }

}

// 에러를 뱉어내려면 원래는, 컨트롤러에서 try catch를 해서 성공과 실패의 응답을 다 써줬어야 했음
