package com.sparta.java_02.domain.user.repository;

import static com.sparta.java_02.domain.user.entity.QUser.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.java_02.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {

  private final JPAQueryFactory queryFactory;  // 이걸 스프링 빈에 등록해서 이걸 di받아서 쓰자!

  public List<User> findUsers(String name, String email) {

    return queryFactory
        .select(user)
        .from(user)
        .where(
//            StringUtils.hasText(name) ? user.name.contains(name) : null,
//            StringUtils.hasText(email) ? user.email.eq(email): null
            nameContains(name),
            emailContains(email)
        )
        .fetch();
  }

  private BooleanExpression nameContains(String name) {
    return StringUtils.hasText(name) ? user.name.contains(name) : null;
  }
  private BooleanExpression emailContains(String email) {
    return StringUtils.hasText(email) ? user.email.contains(email) : null;
  }

}
