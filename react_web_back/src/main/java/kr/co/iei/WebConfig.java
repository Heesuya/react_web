package kr.co.iei;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.co.iei.board.model.dto.BoardDTO;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Value("${file.root}")
	private String root;
	
	@Bean//가져다 쓸데는 Bean 이라는 어노테이션   ,  라이브러리 이것저것 쓸때 Bean 어노테이션 많이 쓰게 될것
	public BCryptPasswordEncoder bCrypt() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) { //Shift+alt+s  "v" 자원접근 허가
		registry
			.addResourceHandler("/editor/**")
			.addResourceLocations("file:///"+root+"/editor/");
		registry
			.addResourceHandler("/board/thumb/**")
			.addResourceLocations("file:///"+root+"/board/thumb/");
	}
	

}
