package kr.co.iei.member.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.iei.member.model.dao.MemberDao;
import kr.co.iei.member.model.dto.LoginMemberDTO;
import kr.co.iei.member.model.dto.MemberDTO;
import kr.co.iei.util.JwtUtils;

@Service
public class MemberService {
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private BCryptPasswordEncoder encoder;
	@Autowired
	private JwtUtils jwtUtil;

	@Transactional
	public int insertMember(MemberDTO member) {
		String encPw = encoder.encode(member.getMemberPw());
		member.setMemberPw(encPw);
		int result = memberDao.insertMember(member);
		return result;
	}

	public int checkId(String memberId) {
		int result = memberDao.checkId(memberId);
		return result;
	}

	public LoginMemberDTO login(MemberDTO member) {
		MemberDTO m = memberDao.selectOneMember(member.getMemberId());//암호화된 비밀번호를 풀 수가 없어서 아이디만 먼저 조회한다.
		//순서 맞아야 함
		if(m != null && encoder.matches(member.getMemberPw(), m.getMemberPw())){//아이디로 조회 성공한 후 패스워드가 맞아야 조회 성공.
			String accessToken = jwtUtil.createAccessToken(m.getMemberId(), m.getMemberType());
			String refeshToken = jwtUtil.createRefreshToken(m.getMemberId(), m.getMemberType());
			//System.out.println(accessToken);
			//System.out.println(refeshToken);
			LoginMemberDTO loginMember = new LoginMemberDTO(accessToken, refeshToken, m.getMemberId(), m.getMemberType());
			return loginMember;
		}
		return null;//return m  이 되면 아이디 객처가 가기 때문에 null로 리턴 될 수 있게 코드 수정
	}

}
