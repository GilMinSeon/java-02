package com.sparta.java_02.domain.user.repository;

import com.sparta.java_02.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

// 이클래스도 jpa가 인지할 수 있게 해야함 => jpa rep ository라는 것을 상속 => interface로 변경(선언체)
@Repository // 이게 repository라고 jpa가 아닌 "spring"한테 알려주는 어노테이션
public interface UserRepository extends JpaRepository<User, Long> {
  //이제 기능을 명세하면 됨. 명세하는 규칙이 있음

  // find 의미 : 있을수도 있고 없을 수도 있음. 반대로 get은 무조건 있는거
  // 그래서 find는 리턴객체에다가 optional 써줘야함
  // name도 조회하고 email도 조회하고싶엉
  //Optional<User> findFirstByNameAndEmail(String name);

  // 그런데 위에처럼 쓰면 복잡하고 길어질 수 있음
  // 아래와 같이 @쿼리 쓰는 방법도 있음. 이렇게 쓸때는 네임규칙 회피해서 쓰기
  // 그런데 정말 필요한 경우에만 쓰고 위에 방법처럼 써라
  @Query("SELECT u FROM User u WHERE u.email = :name")
  Optional<User> findUser(String name);

  Optional<User> findByEmail(String email);


}
