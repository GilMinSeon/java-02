package com.sparta.java_02.domain.user.controller;

import com.sparta.java_02.common.annotation.Loggable;
import com.sparta.java_02.common.response.ApiResponse;
import com.sparta.java_02.domain.user.dto.UserCreateRequest;
import com.sparta.java_02.domain.user.dto.UserResponse;
import com.sparta.java_02.domain.user.dto.UserSearchResponse;
import com.sparta.java_02.domain.user.dto.UserUpdateRequest;
import com.sparta.java_02.domain.user.entity.User;
import com.sparta.java_02.domain.user.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;


  @Loggable
  @GetMapping
  public ApiResponse<List<UserSearchResponse>> findAll() {
    return ApiResponse.success(userService.searchUser());
  }

  @GetMapping("/{userId}")
  public ApiResponse<UserResponse> findById(@PathVariable Long userId) {
    return ApiResponse.success(userService.getUserById(userId));
  }

  @PostMapping
  public ApiResponse<Void> create(@Valid @RequestBody UserCreateRequest request) {
    userService.create(request);
    return ApiResponse.success();
  }

  @PutMapping("/{userId}")
  public ApiResponse<Void> update(@PathVariable Long userId,
      @Valid @RequestBody UserUpdateRequest request) {
    userService.update(userId, request);
    return ApiResponse.success();
  }

  @DeleteMapping("/{userId}")
  public ApiResponse<Void> delete(@PathVariable Long userId) {
    userService.delete(userId);
    return ApiResponse.success();
  }

}