package com.sparta.java_02.domain.user.mapper;

import com.sparta.java_02.domain.user.dto.UserCreateRequest;
import com.sparta.java_02.domain.user.dto.UserResponse;
import com.sparta.java_02.domain.user.dto.UserSearchResponse;
import com.sparta.java_02.domain.user.entity.User;
import org.mapstruct.Mapper;

//@Mapper: 이 인터페이스가 MapStruct 전용 매퍼임을 선언하는 어노테이션
@Mapper(componentModel = "spring")
//→ 이걸 쓰면 Spring이 @Component처럼 인식해서 DI(의존성 주입) 으로 @Autowired나 생성자 주입이 가능해짐
public interface UserMapper {

  //private static final UserMapper USER_MAPPER;  // 이렇게 말고 위에서 mapper 괄호

  // source는 인자, User, 타켓은 리스폰스, 일일이 매핑가능
  //@Mapping(target = "userEmail", source= "email")
  /*
  이 메서드는 User → UserResponse로 바꿔주는 메서드야
  파라미터(User)의 필드를 읽어서, 반환형(UserResponse)의 동일한 이름의 필드에 자동으로 매핑함
   */
  UserResponse toResponse(User user);

  UserSearchResponse toSearch(User user);


  /*
   *이건 DTO → Entity 매핑을 의미
    사용자가 회원가입할 때 보내는 데이터를 받아서,
    DB에 저장할 User 객체로 변환하는 역할
   */
  User toEntity(UserCreateRequest request);
}
// 인자와 리턴값을 정의하면 빌더패턴을 실행할때 알아서 구현해준다
//