package com.kh.SpringJpa241217.controller;

import com.kh.SpringJpa241217.dto.BoardResDto;
import com.kh.SpringJpa241217.dto.CommentReqDto;
import com.kh.SpringJpa241217.dto.CommentResDto;
import com.kh.SpringJpa241217.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "https://localhost:3000") // 이번까지만. 추후엔 뺀다
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor

public class CommentController {
    private final CommentService commentService;

    //댓글 등록
    @PostMapping("/register")
    public ResponseEntity<Boolean> commentRegister(@RequestBody CommentReqDto commentReqDto) {
        boolean isSuccess = commentService.commentRegister(commentReqDto);
        return ResponseEntity.ok(isSuccess);
    }
    // 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteComment(@PathVariable Long commentId, @RequestParam String email) {
        boolean isSuccess = commentService.commentDelete(commentId, email);
        return ResponseEntity.ok(isSuccess);
    }

    // 댓글 목록 조회 (게시글 id)
    @GetMapping("/list")
    public ResponseEntity<List<CommentResDto>> getCommentList(@PathVariable Long boardId) {
        List<CommentResDto> list = commentService.commentList(boardId);
        return ResponseEntity.ok(list);
    }

}
