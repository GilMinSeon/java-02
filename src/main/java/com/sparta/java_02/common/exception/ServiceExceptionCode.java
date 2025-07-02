package com.sparta.java_02.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ServiceExceptionCode {

  NOT_FOUND_DATA("데이터를 찾을 수 없습니다"),
  NOT_FOUND_USER("사용자를 찾을 수 없습니다"),
  OUT_OF_STOCK_PRODUCT("재고없음"),
  CANNOT_CANCEL("취소불가");

  final String message;

}
