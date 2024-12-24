package com.kh.SpringJpa241217.controller;

import com.kh.SpringJpa241217.dto.BoardReqDto;
import com.kh.SpringJpa241217.dto.BoardResDto;
import com.kh.SpringJpa241217.dto.CommentReqDto;
import com.kh.SpringJpa241217.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    // 게시글 등록
    @PostMapping("/save")
    public ResponseEntity<Boolean> saveBoard(@RequestBody BoardReqDto boardReqDto) {
        boolean isSuccess = boardService.saveBoard(boardReqDto);
        return ResponseEntity.ok(isSuccess);
    }
    // 게시글 수정
    @PutMapping("/modify/{id}")
    public ResponseEntity<Boolean> boardModify(@PathVariable Long id, @RequestBody BoardReqDto boardReqDto) {
        boolean isSuccess = boardService.updateBoard(boardReqDto, id);
        return ResponseEntity.ok(isSuccess);
    }

    // 게시글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<BoardResDto> findBoardById(@PathVariable Long id) {
            BoardResDto boardResDto = boardService.findByBoardId(id);
            return ResponseEntity.ok(boardResDto);

    }
    // 게시글 전체 조회
    @GetMapping("/list")
    public ResponseEntity<List<BoardResDto>> findAllBoard() {
        List<BoardResDto> boardList = boardService.findAllBoard();
        return ResponseEntity.ok(boardList);
    }

    //게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteBoard(@PathVariable Long id, @RequestParam String email) {
        boolean isSuccess = boardService.deleteBoard(id,email);
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
    // 게시글 제목 검색
    @GetMapping("/search-title")
    public ResponseEntity<List<BoardResDto>> boardSearchTitle(@RequestParam String keyword) {
        List<BoardResDto> list = boardService.searchBoardByTitle(keyword);
        return ResponseEntity.ok(list);
    }
    // 게시글 제목과 내용검색
    @GetMapping("/search-title-content")
    public ResponseEntity<List<BoardResDto>> boardSearchTitleOrContent (@RequestParam String title, @RequestParam String content) {
        List<BoardResDto> list = boardService.searchBoardByTitleOrContent(title, content);
        return ResponseEntity.ok(list);
    }
    // 12-24
    // 댓글 추가
    @PostMapping("/{boardId}/comments")
    public ResponseEntity<Boolean> addComment(@PathVariable Long boardId, @RequestBody CommentReqDto commentReqDto) {
        boolean isSuccess = boardService.addComment(boardId, commentReqDto);
        return ResponseEntity.ok(isSuccess);
    }
    //12-24
    // 댓글 제거
    @DeleteMapping("/{boardId}/comments/{commentId}")
    public ResponseEntity<Boolean> removeComment(@PathVariable Long boardId, @PathVariable Long commentId) {
        boolean isSuccess = boardService.removeComment(boardId, commentId);
        return ResponseEntity.ok(isSuccess);
    }
}
