package com.sparta.java_02;

import com.sparta.java_02.domain.user.entity.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Java02Application {

	public static void main(String[] args) {

		SpringApplication.run(Java02Application.class, args);

//		User user = new User(name:"이름", email:"이메일", password:"패스워드");
//		save(user); // <- insert 쿼리 실행


		// insert 생성자로 만듬
		// update Setter로 값 변경

		// 빌더패턴
		// 이걸 쓰면 생성자를 쓸때 더 직관적으로 쓸 수 있다. 안쓰면 생성자 순서대로 써야함
		// -> User 클래스에 @Builder
		/*
		User user = User.builder()
				.name("이름")
				.email("이메일")
				.passwordHash("패스워드")
				//만약 패스워드 널이면 빌더패턴 아니면 "" 이렇게 써야하는데 빌더패턴에서는 걍 안쓰면 된다, 알아서 null이 들어감
				.build();
		 */
	}


	// 엔티티는 테이블과 1:1 매칭되서 쓰는애
// 이제 이 테이블을 가져와서 crud 하고 싶다. 이테이블에 대한 기능을 정의하는 클래스 필요 => 그게 repository 클래스
}
