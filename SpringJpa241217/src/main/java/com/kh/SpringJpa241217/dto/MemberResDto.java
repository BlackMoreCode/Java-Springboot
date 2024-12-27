package com.kh.SpringJpa241217.dto;

import com.kh.SpringJpa241217.entity.Board;
import com.kh.SpringJpa241217.entity.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Builder


// 응답을 받을 때는
public class MemberResDto {
    private String email;
//    private String pwd; // 나중에는 보안처리해서 해야한다. 지금은 그대로 다 보일것이다.
    private String name;
    private String imagePath;
    private LocalDateTime regDate;

    // 게시글 목록 추가
    private List<BoardResDto> boards;

    public static MemberResDto of(Member member) {
        return MemberResDto.builder()
                .name(member.getName())
                .email(member.getEmail())
                .imagePath(member.getImgPath())
                .regDate(member.getRegDate())
                .build();
    }
}
