package com.kh.springJpa241217.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResDto {
    private String email;
    private String name;
    private String imagePath;
    private LocalDateTime regDate;

    // 게시글 목록 추가
    private List<BoardResDto> boards;
}
