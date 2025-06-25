package com.sparta.java_02.domain.purchase.service;

import com.sparta.java_02.domain.user.repository.UserRepository;
import com.sparta.java_02.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PurchaseService {

  //private final UserService userService;
  private final UserRepository userRepository;  // 서비스를 주입받지 말고 레포지토리를 가져온다!! 꼭!!
  // 서비스로직이 크게 중복이 되지않는케이스말고는 서비스 참조 X;



}
