package com.sparta.java_02.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.StringUtils;

@Table //(name = "user") cf) 클래스명과 테이블명이 동일한 경우에는 생략해도 됨
@Entity

// 1) 첫번째 방법 클래스에 붙이기
//@Builder
@Getter

// 편의성 어노테이션 2개
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor // 이게 있으면 빈생성자 한줄 안 만들어도됨 public User() {}

@FieldDefaults(level = AccessLevel.PRIVATE) // 필드가 전부 private 으로 됨!
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment 방식을 사용한다는 뜻
  //private Long id;
  Long id;  // FieldDefaults 어노테이션으로 private 접근제한자 제거가능

  @Column(nullable = false, length = 50) //cf) 위에서 pk는 id어노테이션을 쓰면서 테이블의 컬럼이라고 인지, 일반컬럼들은 이 컬럼 어노테이션을 써야 컬럼으로 인지한다
   String name;

  @Column
   String email;

  @Column// cf) name = "password_hash" => name속성도 자동으로 카멜표기법 치환해서 인지해줌, 생략가능
   String passwordHash;  // camel표기법으로 쓴다

  @CreationTimestamp  // 시간이 자동으로 기록
  @Column(nullable = false, updatable = false)
   LocalDateTime createdAt;

  @UpdateTimestamp
  @Column
  LocalDateTime updatedAt;


  // 두번째 방법 생성자에 붙이기 -> 왜냐면 빈생성자가 하나 필요해서
  @Builder
  public User(
      String name,
      String email,
      String passwordHash
  ) {
    this.name = name;
    this.email = email;
    this.passwordHash = passwordHash;
  }

  public void setName(String name) {
    if (StringUtils.hasText(name)) {
      this.name = name;
    }
  }

  public void setEmail(String email) {
    if (StringUtils.hasText(email)) {
      this.email = email;
    }
  }

  public void setPasswordHash(String passwordHash) {
    if (StringUtils.hasText(passwordHash)) {
      this.passwordHash = passwordHash;
    }
  }

}
