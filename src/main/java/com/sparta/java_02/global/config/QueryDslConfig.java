package com.sparta.java_02.global.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryDslConfig { // querydsl에 대한 설정, 5일차
  @PersistenceContext
  private EntityManager entityManager;

  @Bean
  public JPAQueryFactory jpaQueryFactory() {
    return new JPAQueryFactory(entityManager);
  }
}
