package kr.co.iei.admin.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.iei.admin.model.service.AdminServcie;
import kr.co.iei.board.model.dto.BoardDTO;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/admin")
public class AdminController {
	@Autowired
	private AdminServcie adminservice;
	
	@GetMapping(value = "/board/{reqPage}")
	public ResponseEntity<Map> adminBoardList(@PathVariable int reqPage){
		Map map = adminservice.selectAdminBoardList(reqPage);
		return ResponseEntity.ok(map);
	}
	@PatchMapping(value = "/board")
	public ResponseEntity<Integer> changeBoardStatus(@RequestBody BoardDTO board){
		int result = adminservice.changeBoardStatus(board);
		return ResponseEntity.ok(result);
	}
	@GetMapping(value = "/member/{reqPage}")
	public ResponseEntity<Map> adminMemberList(@PathVariable int reqPage){
		Map map = adminservice.selectAdminMemberList(reqPage);
		return ResponseEntity.ok(map);
	}
}
