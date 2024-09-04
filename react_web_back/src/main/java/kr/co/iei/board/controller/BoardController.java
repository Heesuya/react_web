package kr.co.iei.board.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
}
