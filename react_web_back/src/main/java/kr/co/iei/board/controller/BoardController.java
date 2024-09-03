package kr.co.iei.board.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.iei.board.model.service.BoardService;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/board")  //추가적으로 swagger보려면 어노테이션 설정
public class BoardController {
	@Autowired
	private BoardService boardServcie;

	@GetMapping(value = "/list/{reqPage}")
	public ResponseEntity<Map> list (@PathVariable int reqPage){
		//조회결과는 게시물 목록, pageNavi생성 시 필요한 데이터들
		Map map = boardServcie.selectBoardList(reqPage);
		return ResponseEntity.ok(map);
	}
}
