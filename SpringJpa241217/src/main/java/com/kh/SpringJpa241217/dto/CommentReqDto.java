package com.kh.SpringJpa241217.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor
@ToString

public class CommentReqDto { // 댓글 쓰기
    private String email;
    private Long boardId;
    private String content;
}
