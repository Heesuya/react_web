import axios from "axios";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { useRecoilState } from "recoil";
import { loginIdState } from "../utils/RecoilData";
import BoardFrm from "./BoardFrm";
import ToastEditor from "../utils/ToastEditor";

const BoardUpdate = () => {
  const params = useParams();
  const boardNo = params.boardNo;
  const backServer = process.env.REACT_APP_BACK_SERVER;

  const [boardTitle, setBoardTitle] = useState("");
  const [boardContent, setBoardContent] = useState("");
  //파일을 새로 전송하기위한 state
  const [thumbnail, setThumbnail] = useState(null);
  //첨부파일을 새로 전송하기위한 state
  const [boardFile, setBoardFile] = useState([]);
  //조회해온 썸네일을 화면에 보여주기 위한 state
  const [boardThumb, setboardThumb] = useState(null);
  //조회해온 파일목록을 화면에 보여주기 위한 state
  const [fileList, setFileList] = useState([]);
  const [loginId, setLoginId] = useRecoilState(loginIdState);

  const inputTitle = (e) => {
    setBoardTitle(e.target.value);
  };

  console.log("update" + boardThumb);
  useEffect(() => {
    axios
      .get(`${backServer}/board/boardNo/${boardNo}`) //만들어져있는 거 사용~.~해서 데이터 가져옴
      .then((res) => {
        console.log(res);
        setBoardTitle(res.data.boardTitle);
        setBoardContent(res.data.boardContent);
        setboardThumb(res.data.boardThumb);
        setFileList(res.data.fileList);
        console.log("res" + boardThumb);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);
  return (
    <section className="section board-content-wrap">
      <div className="page-title">게시글 수정</div>
      <form
        className="board-write-frm"
        onSubmit={(e) => {
          e.preventDefault();
        }}
      >
        <BoardFrm
          loginid={loginId}
          boardTitle={boardTitle}
          setBoardTitle={inputTitle}
          thumbnail={thumbnail}
          setThumbnail={setThumbnail}
          boardFile={boardFile}
          setBoardFile={setBoardFile}
          boardThumb={boardThumb}
          setboardThumb={setboardThumb}
          fileList={fileList}
          setFileList={setFileList}
        />
        <div className="board-content-wrap">
          <ToastEditor
            boardContent={boardContent}
            setBoardContent={setBoardContent}
            type={1}
          />
        </div>
        <div className="button-zone">
          <button className="btn-primary lg">수정</button>
        </div>
      </form>
    </section>
  );
};
export default BoardUpdate;
