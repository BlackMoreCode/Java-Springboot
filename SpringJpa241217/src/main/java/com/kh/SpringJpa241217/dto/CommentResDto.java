package com.kh.SpringJpa241217.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor
@ToString

public class CommentResDto { // 응답 받을때 = Res(ponse)
    private String email;
    private Long boardId;
    private Long commentId;
    private String content;
    private LocalDateTime regDate;
}
