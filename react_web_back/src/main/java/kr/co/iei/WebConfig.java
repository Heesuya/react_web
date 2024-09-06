package kr.co.iei;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import kr.co.iei.board.model.dto.BoardDTO;
import kr.co.iei.chat.model.service.AllMemberChatHandler;

@Configuration
@EnableWebSocket
public class WebConfig implements WebMvcConfigurer, WebSocketConfigurer {
	@Value("${file.root}")
	private String root;
	
	@Autowired
	private AllMemberChatHandler allMemberChat;
	
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

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// TODO Auto-generated method stub
		registry.addHandler(allMemberChat, "/allChat").setAllowedOrigins("*");//crossorigin과 같은 설정, 다른 url 와도 허용해줘
	}
	
	
	

}
