package com.sparta.java_02.domain.user;

import com.sparta.java_02.domain.user.entity.User;
import com.sparta.java_02.domain.user.service.UserService;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class UserServiceTest {

  @Autowired
  private UserService userService;

  @Autowired
  private EntityManager entityManager;


  @Test
  void saveAllUser() {
    // given
    List<User> users = new ArrayList<>();

    // when
    userService.saveAllUsers(getUsers(2000));

    // then
    User user = entityManager.createQuery(
            "SELECT u FROM User u WHERE u.id = :id",
            User.class
        ).setParameter("id", 2000L)
        .getSingleResult();

  }

  private List<User> getUsers(int count) {
    List<User> users = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      users.add(User.builder()
          .name("name" + i)
          .email("email" + i + "@gmail.com")
          .passwordHash("hash" + i)
          .build());
    }
    return users;
  }
}
