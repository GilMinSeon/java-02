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
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final EntityManager entityManager;
  private final JdbcTemplate jdbcTemplate;

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
    // 방법 1) 빌더 패턴
    //User.builder(): User 엔티티에 Lombok의 @Builder가 붙어 있다는 전제야.
    //각 필드에 .name(...), .email(...)으로 값을 지정해줘
    // 마지막 .build()를 하면 User 객체 하나가 만들어짐
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

  // 대용량배치
  @Transactional
  public void saveAllUsersEntity(List<User> users) {
//    int batchSize = 1000;
//
//    for (int i = 0; i < users.size(); i++) {
//      User user = users.get(i);
//      entityManager.persist(user);  // persist하려면 유저의 실제 아이디를 알아야함
//      // 근데 모름. 디비를 통해서만 알 수 있음
//
//      // 1000건마다 데이터를 내보내기 처리
//      if ((i + 1) % batchSize == 0) {
//        entityManager.flush();
//        entityManager.clear();
//      }
//    }
//    entityManager.flush();
//    entityManager.clear();
//
//    // 문제 -> 영속성에 쌓지 않아서 아이디가 없어서 그걸 구하기 위해서 insert쿼리가 계속 날라감
//    // 만건이면 10번, 만개면 만건의 인서트
//    // autoincrement 의 문제
//
//    // 해결책 : 시퀀스!! 임의의 아이디를 넣을 수 있어서 청크를 가져와서 걍 아이디값 받아와서 넣을 수 있음
//    // mysql은 시퀀스 지원 안함. 우회 필요

    // 방법2) yml에 batchsize 1000쓰면 바로 적용됨, 위의 코드를 대체할 수 있다!
    userRepository.saveAll(users);
  }

  // mysql에서 가능한 방식
  @Transactional
  public void saveAllUsers(List<User> users) {

    String sql = "INSERT INTO user (name, email, password_hash) VALUES (?, ?, ?)";

    jdbcTemplate.batchUpdate(sql, users, 1000, (ps, user) -> {
      LocalDateTime now = LocalDateTime.now();
      ps.setString(1, user.getName());
      ps.setString(2, user.getEmail());
      ps.setString(3, user.getPasswordHash());
    });

  }
}
