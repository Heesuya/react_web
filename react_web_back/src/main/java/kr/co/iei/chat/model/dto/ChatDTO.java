package kr.co.iei.chat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data   //@Alias는 데이터베이스에 메세지 저장할때 필요함 
public class ChatDTO {
	private String type;
	private String memberId;
	private String message;
}
