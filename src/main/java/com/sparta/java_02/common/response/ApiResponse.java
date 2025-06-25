package com.sparta.java_02.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse<T> {

  Boolean result;
  Error error;
  T message;

  public static <T> ApiResponse<T> success() {
    return success(null);
  }

  // 상태코드 200이 자동으로 붙어서 감
  // ResponseEntity를 안거침
  public static <T> ApiResponse<T> success(T message) {
    return ApiResponse.<T>builder()
        .result(true)
        .message(message)
        .build();
  }

  // 200대에러
  // 개발자가 의도한 에러, 200 에러 ex) 아이디 조회했는데 아이디가 없어 그럼 400에러창으로 가버리니까 에러를 캐치해서 핸들링 하고 싶을때!
  // alert을 띄우고 싶을때, ex) 아이디 중복확인??
  // 상태코드 에러안에 코드를 넣어서 리절트 false면 코드를 보고 결정?
  public static <T> ResponseEntity<ApiResponse<T>> error(String code, String errorMessage) {
    return ResponseEntity.ok(ApiResponse.<T>builder()
        .result(false)
        .error(Error.of(code, errorMessage))
        .build());
  }

  // 400대 에러, 404 401 => 이것도 사실 발생하면 안댐. 나는 순간 이멀전씨!!, 토큰없을때 요청하면 401, 프론트에서 잘못요청하면 400에러
  public static <T> ResponseEntity<ApiResponse<T>> badRequest(String code, String errorMessage) {
    return ResponseEntity.badRequest().body(ApiResponse.<T>builder()
        .result(false)
        .error(Error.of(code, errorMessage))
        .build());
  }
  // 500대 에러 -> 이건 발생하면 안댐, 나는 순간 이멀전씨!!
  // 서버로직, 자바 시스템 에러
  public static <T> ResponseEntity<ApiResponse<T>> serverError(String code, String errorMessage) {
    return ResponseEntity.status(500).body(ApiResponse.<T>builder()
        .result(false)
        .error(Error.of(code, errorMessage))
        .build());
  }

  @JsonInclude(JsonInclude.Include.NON_NULL)
  public record Error(String errorCode, String errorMessage) {

    public static Error of(String errorCode, String errorMessage) {
      return new Error(errorCode, errorMessage);
    }
  }
}