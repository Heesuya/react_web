import { useRecoilState, useRecoilValue } from "recoil";
import "./chat.css";
import { isLoginState, loginIdState } from "./RecoilData";
import { Link } from "react-router-dom";
const ChatMain = () => {
  const isLogin = useRecoilValue(isLoginState);
  const [loginId, setLoginId] = useRecoilState(loginIdState);
  return (
    <section className="section chat-wrap">
      <div className="page-title">전체회원 채팅</div>
      {isLogin ? (
        ""
      ) : (
        <div className="login-info-box">
          <h3>로그인 후 이용 가능합니다.</h3>
          <Link to="/login">로그인 페이지로 이동</Link>
        </div>
      )}
    </section>
  );
};
export default ChatMain;
