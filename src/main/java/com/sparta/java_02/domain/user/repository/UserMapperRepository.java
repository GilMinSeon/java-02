package com.sparta.java_02.domain.user.repository;

import com.sparta.java_02.domain.user.dto.SearchUserDto;
import com.sparta.java_02.domain.user.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapperRepository {

  SearchUserDto getUserById(Long id);

  void insertUser(UserDto user);

  void updateUser(UserDto user);

  void deleteUser(Long id);

}
