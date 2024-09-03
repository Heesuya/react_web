import axios from "axios";
import { useEffect, useState } from "react";

const Mypage = () => {
  const backServer = process.env.REACT_APP_BACK_SERVER;
  const [member, setMember] = useState(null);
  const token = "testtoken";
  useEffect(() => {
    axios
      .get(`${backServer}/member`)
      .then((res) => {
        console.log(res);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);
  return (
    <section className="section">
      <div className="page-title">마이페이지</div>
    </section>
  );
};
export default Mypage;
