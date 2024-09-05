package kr.co.iei.board.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kr.co.iei.board.model.dto.BoardDTO;
import kr.co.iei.board.model.dto.BoardFileDTO;
import kr.co.iei.board.model.service.BoardService;
import kr.co.iei.util.FileUtils;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/board")  //추가적으로 swagger보려면 어노테이션 설정
public class BoardController {
	@Autowired
	private BoardService boardServcie;
	
	@Autowired
	private FileUtils fileUtil;
	
	@Value("${file.root}")
	public String root;

	@GetMapping(value = "/list/{reqPage}")
	public ResponseEntity<Map> list (@PathVariable int reqPage){
		//조회결과는 게시물 목록, pageNavi생성 시 필요한 데이터들
		Map map = boardServcie.selectBoardList(reqPage);
		return ResponseEntity.ok(map);
	}
	@PostMapping(value = "/editorImage")
	public ResponseEntity<String> editorImage (@ModelAttribute MultipartFile image){
		String savepath = root+"/editor/";
		String filepath = fileUtil.upload(savepath, image);
		return ResponseEntity.ok("/editor/"+filepath);//editor , fileUtile 같은 경우 어디서든 써도 사용가능한 형태로 만들면 된다. 
	}
	@PostMapping
	public ResponseEntity<Boolean> insertBoard
											(@ModelAttribute BoardDTO board, 
											@ModelAttribute MultipartFile thumbnail, 
											@ModelAttribute MultipartFile[] boardFile){
		System.err.println(board);
		if(thumbnail != null) {
			String savepath = root+"/board/thumb/";
			String filepath = fileUtil.upload(savepath, thumbnail);
			board.setBoardThumb(filepath);
		}
		List<BoardFileDTO> boardFileList = new ArrayList<BoardFileDTO>();
		if(boardFile != null) {
			String savepath = root +"/board/";
			for(MultipartFile file : boardFile) {
				BoardFileDTO fileDTO = new BoardFileDTO();
				String filename = file.getOriginalFilename();
				String filepath = fileUtil.upload(savepath, file);
				fileDTO.setFilename(filename);
				fileDTO.setFilepath(filepath);
				boardFileList.add(fileDTO);
			}
		}
		int result = boardServcie.insertBoard(board, boardFileList);
		return ResponseEntity.ok(result == 1 +boardFileList.size());
	}
	
	@GetMapping(value = "/boardNo/{boardNo}")
	public ResponseEntity<BoardDTO> selectOneBoard(@PathVariable int boardNo){
		BoardDTO board = boardServcie.selectOneBoard(boardNo);
		return ResponseEntity.ok(board);
	}
	
	@GetMapping(value = "/file/{boardFileNo}")//특정데이터가 아닌 자원(파일리소스)자체를 되돌려 주겠다   //throws 스프링 컨테인에 에러처리
	public ResponseEntity<Resource> filedown(@PathVariable int boardFileNo) throws FileNotFoundException{
		BoardFileDTO boardFile = boardServcie.getBoardFile(boardFileNo);
		String savepath = root+"/board/";
		File file = new File(savepath+boardFile.getFilepath());
		Resource resource = new InputStreamResource(new FileInputStream(file));
		//파일다운로드를 위한 헤더 설정 
		HttpHeaders header = new HttpHeaders();
		//header.add("Content-Disposition", "attachment; filename=\""+boardFile.getFilename()+"\"");//파일의 이름 //filename="filename" 
		//캐시서버 둠 - 넷플릭스 등등 사용하지만, 우리는 사용지 않아서 캐시 사용하지 않음 설정함
		header.add("Cache-Control", "no-cache, no-store, must-revalidate");
		header.add("Pragma", "no-cache");//옛날 브라우저에게 알려줄때 설정
		header.add("Expires", "0");//전송 끝나면 0을 알려주겠다 
		
		return ResponseEntity
					.status(HttpStatus.OK)
					.headers(header)
					.contentLength(file.length()) //파일용량 크면 얼마나 걸리는지
					.contentType(MediaType.APPLICATION_OCTET_STREAM)
					.body(resource);
	}
	
	@DeleteMapping(value = "/{boardNo}")
	public ResponseEntity<Integer> deleteBoard(@PathVariable int boardNo){
		List<BoardFileDTO> delFileList = boardServcie.deleteBoard(boardNo);
		if(delFileList != null) {
			String savepath = root+"/board/";
			for(BoardFileDTO boardFile : delFileList) {
				File delFile = new File(savepath + boardFile.getFilepath());
				delFile.delete();
			}
			return ResponseEntity.ok(1);
		}else {
			return ResponseEntity.ok(0);
		}
	}
	@PatchMapping
	public ResponseEntity<Boolean> updateBoard(@ModelAttribute BoardDTO board, 
										@ModelAttribute MultipartFile thumbnail, 
										@ModelAttribute MultipartFile[] boardFile){
		//System.out.println(board.getDelBoardFileNo());
		if(thumbnail != null) { //썸네일 작업
			String savepath = root+"/board/thumb/";
			String filepath = fileUtil.upload(savepath, thumbnail);
			board.setBoardThumb(filepath);
		}
		List<BoardFileDTO> boardFileList = new ArrayList<BoardFileDTO>();
		if(boardFile != null) {//새첨부파일 작업
			String savepath = root+"/board/";
			for(MultipartFile file : boardFile) {
				BoardFileDTO boardFileDTO = new BoardFileDTO();
				String filename = file.getOriginalFilename();
				String filepath = fileUtil.upload(savepath, file);
				boardFileDTO.setFilename(filename);
				boardFileDTO.setFilepath(filepath);
				boardFileDTO.setBoardNo(board.getBoardNo());
				boardFileList.add(boardFileDTO);
			}
		}
		List<BoardFileDTO> delFileList = boardServcie.updateBoard(board, boardFileList);
		if(delFileList != null) {
			String savepath = root+"/board/";
			for(BoardFileDTO deleteFile : delFileList) {
				File delFile = new File(savepath+deleteFile.getFilepath());
				delFile.delete();
			}
			return ResponseEntity.ok(true);
		}else {
			return ResponseEntity.ok(false);
		}
	}
}
