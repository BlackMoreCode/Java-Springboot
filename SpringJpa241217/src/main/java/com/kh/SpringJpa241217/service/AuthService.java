package com.kh.SpringJpa241217.service;

import com.kh.SpringJpa241217.dto.LogInReqDto;
import com.kh.SpringJpa241217.dto.MemberReqDto;
import com.kh.SpringJpa241217.entity.Member;
import com.kh.SpringJpa241217.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

//비즈니스 로직


@Slf4j // 로그 정보를 출력하기 위함
@Service // 스프링 컨테이너에 빈(객체)를 등록
@RequiredArgsConstructor // 생성자를 자동으로 생성
@Transactional // 여러개의 작업을 하나의 논리적인 단위로 묶음

public class AuthService {
    //생성자를 통한 의존성 주입;
    //생성자를 통해서 의존성 주입을 받는 경우 Autowired를 생략.
    private final MemberRepository memberRepository;


    //회원 가입 여부
    public boolean isMember(String email) {
        // email 있으면 true, 아니면 false 넘긴다.
        return memberRepository.existsByEmail(email);
    }

    //회원 가입
    public boolean signUp(MemberReqDto memberReqDto) {
        try {
            Member member = convertDtoToEntity(memberReqDto);
            memberRepository.save(member); // 회원가입, save() insert, update 역할
            return true;
        } catch (Exception e) {
            log.error("회원 가입 실패 : {} ", e.getMessage());
            return false;
        }
    }

    //로그인 (토큰 발행을 이 시점에서; JWT를 쓸 때 주의해야할 점)
    public boolean login(LogInReqDto logInReqDto) {
        // Null 방지용으로 껍데기를 씌우는게 Optional
        try {
            Optional<Member> member = memberRepository
                    .findByEmailAndPwd(logInReqDto.getEmail(), logInReqDto.getPwd());
            return member.isPresent(); // isPresent()는 해당 객체가 있음을 의미; 존재하면 true 값 넘어온다.
        } catch(Exception e) {
            log.error("로그인 실패 : {} ", e.getMessage());
            return false;
        }
    }

    //회원 가입 dto --> Entity (변환 메서드)
    private Member convertDtoToEntity(MemberReqDto memberReqDto) {
        Member member = new Member();
        member.setEmail(memberReqDto.getEmail());
        member.setName(memberReqDto.getName());
        member.setPwd(memberReqDto.getPwd());
        return member;
    }
}
