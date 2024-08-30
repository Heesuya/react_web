package kr.co.iei;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class WebConfig {
	@Bean//가져다 쓸데는 Bean 이라는 어노테이션   ,  라이브러리 이것저것 쓸때 Bean 어노테이션 많이 쓰게 될것
	public BCryptPasswordEncoder bCrypt() {
		return new BCryptPasswordEncoder();
	}
}
