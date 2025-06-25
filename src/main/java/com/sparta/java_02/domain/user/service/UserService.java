package com.sparta.java_02.domain.user.service;

import com.sparta.java_02.common.exception.ServiceException;
import com.sparta.java_02.common.exception.ServiceExceptionCode;
import com.sparta.java_02.domain.user.dto.UserCreateRequest;
import com.sparta.java_02.domain.user.dto.UserResponse;
import com.sparta.java_02.domain.user.dto.UserSearchResponse;
import com.sparta.java_02.domain.user.dto.UserUpdateRequest;
import com.sparta.java_02.domain.user.entity.User;
import com.sparta.java_02.domain.user.mapper.UserMapper;
import com.sparta.java_02.domain.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Transactional
  public List<UserSearchResponse> searchUser() {
//    return userRepository.findAll().stream()
//        .map((User user) -> UserSearchResponse.builder()
//            .id(user.getId())
//            .name(user.getName())
//            .email(user.getEmail())
//            .createdAt(user.getCreatedAt())
//            .build())
//        .toList();
      return userRepository.findAll().stream()
          .map(userMapper::toSearch)
          //.map(user -> userMapper.toSearch(user))
          .toList();
  }

  @Transactional
  public UserResponse getUserById(Long userId) {
//    User user = getUser(userId);
//    return UserResponse.builder()
//        .id(user.getId())
//        .name(user.getName())
//        .email(user.getEmail())
//        .createdAt(user.getCreatedAt())
//        .build();
    return userMapper.toResponse(getUser(userId));
  }

  @Transactional
  public void create(UserCreateRequest request) {
//    userRepository.save(User.builder()
//        .name(request.getName())
//        .email(request.getEmail())
//        .passwordHash(request.getPassword())
//        .build());

      // mapstruct 활용방법
      userRepository.save(userMapper.toEntity(request));
  }

  @Transactional
  public void update(Long userId, UserUpdateRequest request) {
    User user = getUser(userId);

    user.setName(request.getName());
    user.setEmail(request.getEmail());

    userRepository.save(user);
  }

  @Transactional
  public void delete(Long userId) {
    userRepository.delete(getUser(userId));
  }

  public User getUser(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));
  }

}
