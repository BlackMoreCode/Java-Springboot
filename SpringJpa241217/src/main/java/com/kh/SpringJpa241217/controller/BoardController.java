package com.kh.SpringJpa241217.controller;

import com.kh.SpringJpa241217.dto.BoardReqDto;
import com.kh.SpringJpa241217.dto.BoardResDto;
import com.kh.SpringJpa241217.dto.MemberReqDto;
import com.kh.SpringJpa241217.service.BoardService;
import io.swagger.models.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "https://localhost:3000") // 이번까지만. 추후엔 뺀다
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor

//실습
// 1.POSTMAN으로 회원 존재여부 확인, 회원가입, 로그인
// 2.게시글 조회  (POSTMAN)
// 3. SWAGGER 등록 후 게시글 존재여부 확인, 게시글 등록, 게시글 수정, 게시글 삭제, etc

public class BoardController {
    private final BoardService boardService;

    //게시글 전체 조회
    @GetMapping("/list")
    public ResponseEntity<List<BoardResDto>> getBoardList() {
        List<BoardResDto> list = boardService.findAllBoard();
        return ResponseEntity.ok(list);
    }

    // 게시글 상세 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<BoardResDto> boardDetail(@PathVariable Long id) {
        BoardResDto boardResDto = boardService.findByBoardId(id);
        return ResponseEntity.ok(boardResDto);
    }


    // 게시글 등록
    @PostMapping("/register")
    public ResponseEntity<Boolean> register(@RequestParam BoardReqDto boardReqDto) {
        boolean isSuccess = boardService.saveBoard(boardReqDto);
        return ResponseEntity.ok(isSuccess);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<Boolean> updateBoardContent(@PathVariable Long id, @RequestBody BoardReqDto boardReqDto) {
        log.info("게시글 업데이트 시도 :  {}", boardReqDto);
        boolean isSuccess = boardService.updateBoardContent(id, boardReqDto);
        return ResponseEntity.ok(isSuccess);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteBoard(@PathVariable Long id, @RequestParam String email) {
        boolean isSuccess = boardService.deleteBoard(id, email);
        return ResponseEntity.ok(isSuccess);
    }

    // 게시글 페이징 카운트
    @GetMapping("/count")
    public ResponseEntity<Integer> boardPageCnt(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        Integer pageCnt = boardService.getBoardPageCount(page, size);
        return ResponseEntity.ok(pageCnt);
    }

    // 게시글 페이징 조회
    @GetMapping("/list/page")
    public ResponseEntity<List<BoardResDto>> boardPageList(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        List<BoardResDto> list = boardService.pagingBoardList(page, size);
        return ResponseEntity.ok(list);
    }

    // 게시글 제목으로 검색
    @GetMapping("/search-title")
    public ResponseEntity<List<BoardResDto>> boardSearchTitle (@RequestParam String keyword) {
        List<BoardResDto> list = boardService.searchBoard(keyword);
        return ResponseEntity.ok(list);
    }

    // 게시글 제목과 내용 검색
    @GetMapping("/search-title-content")
    public ResponseEntity<List<BoardResDto>> boardSearchTitleOrContent (@RequestParam String title, String content) {
        List<BoardResDto> list = boardService.searchBoardTitleOrContent(title, content);
        return ResponseEntity.ok(list);
    }

}
