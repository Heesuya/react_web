import { useRef, useState } from "react";
import "./member.css";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Swal from "sweetalert2";
const Join = () => {
  const backServer = process.env.REACT_APP_BACK_SERVER; //process.env. 고정  [환경설정된 데이터값 가져오기]
  const navigate = useNavigate();
  const [member, setMember] = useState({
    memberId: "",
    memberPw: "",
    memberName: "",
    memberPhone: "",
  });
  //아이디 중복체크 결과에 따라서 바뀔 state
  //0 : 검사하지 않은 상태, 1 : 정규표현식,중복체크 모두 통과한 경우
  //2 : 정규표현식을 만족하지 못한 상태, 3 : 아이디가 중복인 경우
  const [idCheck, setIdCheck] = useState(0);
  const checkId = () => {
    //아이디 유효셩검사
    //1. 정규표현식 검사
    //2. 정규표현식 검사 성공하면, DB에 중복체크
    const idReg = /^[a-zA-Z0-9]{4,8}$/;
    if (!idReg.test(member.memberId)) {
      setIdCheck(2);
    } else {
      axios
        .get(`${backServer}/member/memberId/${member.memberId}/check-id`) //회원 정보 get 하는게 아니라 확인 이기에, 주소 한개 더 다르게 처리
        .then((res) => {
          console.log(res);
          if (res.data === 1) {
            setIdCheck(3);
          } else if (res.data === 0) {
            setIdCheck(1);
          }
        })
        .catch((err) => {
          console.log(err);
        });
    }
  };
  const changeMember = (e) => {
    const name = e.target.name;
    setMember({ ...member, [name]: e.target.value });
  };
  const [memberPwRe, seMemberPwRe] = useState("");
  const chageMemberPwRe = (e) => {
    seMemberPwRe(e.target.value);
  };
  //console.log(member);
  //console.log(memberPwRe);
  //비밀번호 / 비밀번호 확인 이 같은지 체크해서 메세지 출력
  //react에서 요소를 선택할 때 document.querySelector() 방식 권고하지 않음    dom 이 html에서 객체화 한것 = real dom / react는 virtual dom 사용 인데 갑자기 리얼돔 쓰면 혼돈스럽다.
  //useRef 훅을 이용해서 요소와 연결해서 사용(연결하고 싶은 태그의 ref속성에 해당 객체를 적용)
  //연결이 되면 해당 객체의 current속성이 dom객체를 의미
  const pwMessage = useRef(null); //reference 참조하겠다, 예전 방식이 아닌 useRef 훅을 생성해서 연결하고 싶은 태그에 ref 속성으로 연결하면 된다.
  const checkPw = () => {
    pwMessage.current.classList.remove("valid");
    pwMessage.current.classList.remove("invalid");
    if (member.memberPw === memberPwRe) {
      //console.log("비밀번호 일치");
      pwMessage.current.classList.add("valid");
      pwMessage.current.innerText = "비밀번호가 일치합니다.";
    } else {
      //console.log("비밀번호가 일치하지 않을 때");
      pwMessage.current.classList.add("invalid");
      pwMessage.current.innerText = "비밀번호가 일치하지 않습니다";
    }
  };
  const join = () => {
    if (idCheck === 1 && pwMessage.current.classList.contains("vaild")) {
      //유효성이 만족하면 axios 가 돌아가게 만든다.
      axios
        .post(`${backServer}/member`, member) //변수를 보낼때는 `` 사용하자 backServer+"/member" 와 같은 형태
        .then((res) => {
          //console.log(res);
          navigate("/login");
        })
        .catch((err) => {
          //console.log(err);
        });
    } else {
      Swal.fire({
        text: "입력 값을 확인하세요",
        icon: "info",
        confirmButtonColor: "var(--main3)",
      });
    }
  };
  return (
    <section className="section join-wrap">
      <div className="page-title">회원가입</div>
      <form
        onSubmit={(e) => {
          e.preventDefault();
          //console.log("회원가입버튼 클릭"); submit 버튼 누르면 작동하는 함수, 내가 진행하는 함수로 실행하겠다.
          join();
        }}
      >
        <div className="input-wrap">
          <div className="input-title">
            <label htmlFor="memberId">아이디</label>
          </div>
          <div className="input-item">
            <input
              type="text"
              name="memberId"
              id="memberId"
              value={member.memberId}
              onChange={changeMember}
              onBlur={checkId}
            ></input>
          </div>
          <p
            className={
              "input-msg" +
              (idCheck === 0 ? "" : idCheck === 1 ? " valid" : " invalid")
            }
          >
            {idCheck === 0
              ? ""
              : idCheck === 1
              ? "사용가능한 아이디 입니다."
              : idCheck === 2
              ? "아이디는 영어 대/소문자 숫자로 4~8글자 입니다."
              : "이미 사용중인 아이디입니다."}
          </p>
        </div>
        <div className="input-wrap">
          <div className="input-title">
            <label htmlFor="memberPw">비밀번호</label>
          </div>
          <div className="input-item">
            <input
              type="password"
              name="memberPw"
              id="memberPw"
              value={member.memberPw}
              onChange={changeMember}
            ></input>
          </div>
        </div>
        <div className="input-wrap">
          <div className="input-title">
            <label htmlFor="memberPwRe">비밀번호 확인</label>
          </div>
          <div className="input-item">
            <input
              type="password"
              name="memberPwRe"
              id="memberPwRe"
              value={memberPwRe}
              onChange={chageMemberPwRe}
              onBlur={checkPw}
            ></input>
          </div>
          <p className="input-msg" ref={pwMessage}></p>
        </div>
        <div className="input-wrap">
          <div className="input-title">
            <label htmlFor="memberName">이름</label>
          </div>
          <div className="input-item">
            <input
              type="text"
              name="memberName"
              id="memberName"
              value={member.memberName}
              onChange={changeMember}
            ></input>
          </div>
        </div>
        <div className="input-wrap">
          <div className="input-title">
            <label htmlFor="memberPhone">전화번호</label>
          </div>
          <div className="input-item">
            <input
              type="text"
              name="memberPhone"
              id="memberPhone"
              value={member.memberPhone}
              onChange={changeMember}
            ></input>
          </div>
        </div>
        <div className="join-button-box">
          <button type="submit" className="btn-primary lg">
            회원가입
          </button>
        </div>
      </form>
    </section>
  );
};
export default Join;
