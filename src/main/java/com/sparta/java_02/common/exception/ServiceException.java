package com.sparta.java_02.common.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceException extends RuntimeException {

    String code;
    String message;

    public ServiceException(ServiceExceptionCode code) {
      super(code.getMessage()); // 부모의 생성자를 선언하면서 겟메세지를 넣어줄 수 있다. 부모클래스의 생성자
      this.code = code.name();
      this.message = super.getMessage();
    }

    @Override
    public String getMessage() {
      return message;
    }
}
