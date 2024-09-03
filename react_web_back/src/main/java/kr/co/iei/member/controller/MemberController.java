package kr.co.iei.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.iei.member.model.dto.LoginMemberDTO;
import kr.co.iei.member.model.dto.MemberDTO;
import kr.co.iei.member.model.service.MemberService;

@CrossOrigin("*")
@RestController
@RequestMapping(value="/member")
@Tag(name="MEBER", description = "MEMBER API")
public class MemberController {
	@Autowired
	private MemberService memberService;
	
	@PostMapping         //제네릭은 객체 타입만 가능해서 int가 아닌 Integer 
	public ResponseEntity<Integer> join(@RequestBody MemberDTO member) {//어제와 다르게 객체타입이 아닌 주고싶은 형태로 보낸다
		int result = memberService.insertMember(member);
		if(result > 0) {
			return ResponseEntity.ok(result);
		}else {
			return ResponseEntity.status(500).build();
		}
	}
	@GetMapping(value="memberId/{memberId}/check-id")
	public ResponseEntity<Integer> checkId(@PathVariable String memberId){
		int result = memberService.checkId(memberId);
		return ResponseEntity.ok(result);
	}
	@PostMapping(value="login") //session.add 하지만 이제는 안함. single-page-application을 하는데 session의 의미가 사라짐. 못쓰는게 아니라 안쓰는거임ㄴ
	public ResponseEntity<LoginMemberDTO> login(@RequestBody MemberDTO member){
		LoginMemberDTO loginMember = memberService.login(member);
		if(loginMember != null) {
			return ResponseEntity.ok(loginMember);
		}else {
			return ResponseEntity.status(404).build();
		}
	}
	//토큰 작성하는 방법 보기 위한 로직
	/*
	@GetMapping
	public ResponseEntity<MemberDTO> getMember(@RequestHeader("Authorization") String token){
		System.out.println("token"+token); //mypage 이동시 받은 토큰
		MemberDTO member = memberService.selectOneMember(token);
		return ResponseEntity.ok(null);
	}
	*/

	@PostMapping(value = "/refresh")
	public ResponseEntity<LoginMemberDTO> refresh(@RequestHeader("Authorization") String token){
		LoginMemberDTO loginMember = memberService.refresh(token);
		if(loginMember != null) {
			return ResponseEntity.ok(loginMember);
		}else {
			return ResponseEntity.status(404).build();
		}
			
	}
	
	@GetMapping
	public ResponseEntity<MemberDTO> selectOneMember(@RequestHeader("Authorization") String token){

		MemberDTO member = memberService.selectOneMember(token);
		return ResponseEntity.ok(member);
	}
	
	@PatchMapping
	public ResponseEntity<Integer> updatemember(@RequestBody MemberDTO member){
		int result = memberService.updateMember(member);
		return ResponseEntity.ok(result);
	}
	
	@PostMapping(value = "/pw")
	public ResponseEntity<Integer> checkPw(@RequestBody MemberDTO member){
		int result = memberService.checkPw(member);
		return ResponseEntity.ok(result);
	}

	@PatchMapping(value = "/pw")
	public ResponseEntity<Integer> changePw(@RequestBody MemberDTO member){
		int result = memberService.changePw(member);
		return ResponseEntity.ok(result);
	}
	
	@DeleteMapping                               //보내주는 정보가 없어도 로그인이 되어 있으면 토큰 요청 가능
	public ResponseEntity<Integer> deleteMember(@RequestHeader("Authorization") String token){
		int result = memberService.deleteMember(token);
		return ResponseEntity.ok(result);
	}
}
