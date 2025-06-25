package com.sparta.java_02.domain.user.mapper;

import com.sparta.java_02.domain.user.dto.UserCreateRequest;
import com.sparta.java_02.domain.user.dto.UserResponse;
import com.sparta.java_02.domain.user.dto.UserSearchResponse;
import com.sparta.java_02.domain.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") // 이렇게 하면 유저매퍼쓸수있음 DIㄱ 나으
public interface UserMapper {

  //private static final UserMapper USER_MAPPER;  // 이렇게 말고 위에서 mapper 괄호

  // source는 인자, User, 타켓은 리스폰스, 일일이 매핑가능
  //@Mapping(target = "userEmail", source= "email")
  UserResponse toResponse(User user);

  UserSearchResponse toSearch(User user);

  User toEntity(UserCreateRequest request);
}
// 인자와 리턴값을 정의하면 빌더패턴을 실행할때 알아서 구현해준다
//