package com.kh.SpringJpa241217.dto;

import com.kh.SpringJpa241217.constant.Authority;
import com.kh.SpringJpa241217.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

// DTO: 다른 레이어간의 데이터를 교환할 때 사용;
// 주로 FrontEnd/BackEnd 사이에서 데이터를 주고 받는 용도
// 회원 가입용 예제
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class MemberReqDto {
    // 이하 3개 이상 넣을 필요가 없다; 더 들어가면 프론트에서 헷갈릴지도?
    private String email;
    private String pwd;
    private String name;
    private String imgPath;

    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                .pwd(passwordEncoder.encode(pwd))
                .name(name)
                .img(imgPath)
                .authority(Authority.ROLE_USER)
                .build();
    }
    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, pwd);
    }
}
