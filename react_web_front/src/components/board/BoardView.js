import { Viewer } from "@toast-ui/react-editor";
import axios from "axios";
import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { useRecoilState } from "recoil";
import { loginIdState } from "../utils/RecoilData";
import Swal from "sweetalert2";

const BoardView = () => {
  const backServer = process.env.REACT_APP_BACK_SERVER;
  const navigate = useNavigate();
  const params = useParams();
  const boardNo = params.boardNo;
  const [board, setBoard] = useState({});
  const [loginId, setLoginId] = useRecoilState(loginIdState);
  useEffect(() => {
    axios
      .get(`${backServer}/board/boardNo/${boardNo}`)
      .then((res) => {
        console.log(res);
        setBoard(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);
  const deleteBoard = () => {
    axios
      .delete(`${backServer}/board/${board.boardNo}`)
      .then((res) => {
        console.log(res);
        if (res.data === 1) {
          navigate("/board/list");
        } else {
          Swal.fire({
            text: "관리자 문의",
          });
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };
  return (
    <section className="section board-view-wrap">
      <div className="page-title">게시글</div>
      <div className="board-view-content">
        <div className="board-view-info">
          <div className="board-thumbnail">
            <img
              src={
                board.boardThumb
                  ? `${backServer}/board/thumb/${board.boardThumb}`
                  : "/image/default_img.png"
              }
            />
          </div>
          <div className="board-view-preview">
            <table className="tbl">
              <tbody>
                <tr>
                  <td className="left" colSpan={4}>
                    {board.boardTitle}
                  </td>
                </tr>
                <tr>
                  <th style={{ width: "20%" }}>작성자</th>
                  <td style={{ width: "30%" }}>{board.boardWriter}</td>
                  <th style={{ width: "20%" }}>작성일</th>
                  <td style={{ width: "30%" }}>{board.boardDate}</td>
                </tr>
              </tbody>
            </table>
            <p className="file-title">첨부파일</p>
            <div className="file-zone">
              {board.fileList
                ? board.fileList.map((file, i) => {
                    return <FileItem key={"file" + i} file={file} />;
                  })
                : ""}
            </div>
          </div>
        </div>
        {/*
        html로 저장된 데이터를 화면에 띄울때는 {} 문법이아니라 속성을 이용해서 표현        
        <div
          className="board-content-wrap"
          dangerouslySetInnerHTML={{ __html: board.boardContent }}
        ></div>
         */}
        {/* toast-editor 는 뷰어를 제공해서 안전한 점이 큰 장점이다 */}
        <div className="board-content-wrap">
          {board.boardContent ? (
            <Viewer initialValue={board.boardContent} />
          ) : (
            ""
          )}
        </div>
        {loginId === board.boardWriter ? (
          <div className="view-btn-zone">
            <Link to="#" className="btn-primary lg">
              수정
            </Link>
            <button
              type="button"
              className="btn-secondary lg"
              onClick={deleteBoard}
            >
              삭제
            </button>
          </div>
        ) : (
          ""
        )}
      </div>
    </section>
  );
};
const FileItem = (props) => {
  const file = props.file;
  const backServer = process.env.REACT_APP_BACK_SERVER;
  const filedown = () => {
    //기존 일반요청과 다름.
    axios
      .get(`${backServer}/board/file/${file.boardFileNo}`, {
        //axios는 기본적으로 응답을 json으로 처리
        //현재 요청은 일반적인 json타입의 응답을 받을게 아니라 파일을 받아야 함
        //일반적인 json으로 처리 불가능 -> 파일로 결과를 받는 설정
        responseType: "blob",
      })
      .then((res) => {
        console.log(res);
        //서버에서 받은 데이터를 javascript의 blob객체로 변환
        const blob = new Blob([res.data]); //기존 가상의 a태그 만들어서 다운하며 없앰
        //blob데이터를 이용해서 데이터 객체 url생성(다운로드할수 있는 링크) //동기형태처럼
        const fileObjectUrl = window.URL.createObjectURL(blob);

        //데이터를 다운로드할 링크 생성
        const link = document.createElement("a");
        link.href = fileObjectUrl;
        link.style.display = "none";
        //다운로드할 파일명 지정
        link.download = file.filename;
        //파일이랑 연결한 a태그를 문서에 포함
        document.body.appendChild(link);
        link.click(); //추가한 a태그를 클릭해서 다운로드
        link.remove(); //다운로드 후 a태그 삭제
        window.URL.revokeObjectURL(fileObjectUrl); //파일링크 삭제
      })
      .catch((err) => {
        console.log(err);
      });
  };
  return (
    <div className="board-file">
      <span className="material-icons file-icon" onClick={filedown}>
        file_download
      </span>
      <span className="file-name">{file.filename}</span>
    </div>
  );
};
export default BoardView;
