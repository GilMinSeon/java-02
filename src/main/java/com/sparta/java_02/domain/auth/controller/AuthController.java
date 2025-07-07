package com.sparta.java_02.domain.auth.controller;

import com.sparta.java_02.common.exception.ServiceException;
import com.sparta.java_02.common.exception.ServiceExceptionCode;
import com.sparta.java_02.common.response.ApiResponse;
import com.sparta.java_02.domain.auth.dto.LoginRequest;
import com.sparta.java_02.domain.auth.dto.LoginResponse;
import com.sparta.java_02.domain.auth.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j  // 로그 찍으려고
@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ApiResponse<LoginResponse> login(HttpSession session,
      @Valid @RequestBody LoginRequest request) {

    LoginResponse loginResponse = authService.login(request);

    session.setAttribute("userId", loginResponse.getUserId());
    session.setAttribute("name", loginResponse.getName());
    session.setAttribute("email", loginResponse.getEmail());

    log.info("session id : {}", session.getId());

    return ApiResponse.success(authService.login(request));
  }

  @GetMapping("/status")
  public ApiResponse<LoginResponse> getStatus(HttpSession session) {

    Long userId = (Long) session.getAttribute("userId");
    String name = (String) session.getAttribute("name");
    String email = (String) session.getAttribute("email");

    if (ObjectUtils.isEmpty(userId) && ObjectUtils.isEmpty(name) && ObjectUtils.isEmpty(email)) {
      throw new ServiceException(ServiceExceptionCode.NOT_FOUND_DATA);
    }

    return ApiResponse.success(authService.getLoginResponse(userId, name, email));
  }

  @GetMapping("/logout")
  public ApiResponse<Void> logout(HttpSession httpSession) {
    httpSession.invalidate();
    return ApiResponse.success();
  }
}

// 로그인을 한 다음 겟 스테이터스를 하면 유저정보를 받아오고 싶은거
// 세션이 없다면 로그인 정보를 받을 수 없음
// 1차캐시

// 실습
// 로그인 -> 세션확인 -> 로그아웃해서 세션이 어떻게 움직이는지
// invalidate => 세션 무효화, 로그아웃

// 1차캐시 => 요청과 jpa의1차캐시랑 다름. http 요청과 서버 사이에서 캐싱을 하나할건데 그게 첫번째 캐시다