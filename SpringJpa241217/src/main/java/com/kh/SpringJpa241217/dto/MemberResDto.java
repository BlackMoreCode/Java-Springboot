package com.kh.SpringJpa241217.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


// 응답을 받을 때는
public class MemberResDto {
    private String email;
//    private String pwd; // 나중에는 보안처리해서 해야한다. 지금은 그대로 다 보일것이다.
    private String name;
    private String imagePath;
    private LocalDateTime regDate;
}
