package com.kh.SpringJpa241217.service;

import com.kh.SpringJpa241217.dto.BoardResDto;
import com.kh.SpringJpa241217.dto.MemberReqDto;
import com.kh.SpringJpa241217.dto.MemberResDto;
import com.kh.SpringJpa241217.entity.Board;
import com.kh.SpringJpa241217.entity.Member;
import com.kh.SpringJpa241217.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j // 로그 정보를 출력하기 위함
@Service // 스프링 컨테이너에 빈(객체)를 등록
@AllArgsConstructor // 생성자를 통한 의존성 주입을 받기 위해서 생성자를 롬복을 통해서 생성
@Transactional // 여러개의 작업을 하나의 논리적인 단위로 묶음

public class MemberService {
    private final MemberRepository memberRepository;


    //회원 전체 조회 내가 시도한 방식
//    public List<Member> getAllMembers() {
//        return memberRepository.findAll();
//    }

    //회원 정보 조회 (내가 시도한 예시)
    // email을 기반으로 특정 유저의 정보를 조회 --> memberRepository에 있는 findByEmail을 사용
//    public Member getMemberDetail(String email) {
//        return memberRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("해당 이메일을 가진 유저가 없습니다 : " + email));
//    }

    //회원 전체 조회 수업중에 보여진 방식. 새로운 메모리를 만듬.
    // 리스트를 여기서 만든다.
    public List<MemberResDto> getMemberList() {
        List<Member> members = memberRepository.findAll();
        List<MemberResDto> memberDtos = new ArrayList<>();
        for(Member member : members) {
            memberDtos.add(MemberResDto.of(member));
        }
        return memberDtos;
    }

    // Member Entity => MemberResDto 로 변환
    private MemberResDto convertEntityToDto(Member member) {
        MemberResDto memberResDto = new MemberResDto();
        memberResDto.setEmail(member.getEmail());
        memberResDto.setName(member.getName());
        memberResDto.setRegDate(member.getRegDate());
        memberResDto.setImagePath(member.getImgPath());
        return memberResDto;
    }

    // 회원 정보 조회 (수업 예시)
    public MemberResDto getMemberDetail(String email) {
        // 최종적으로는 이하와 같이 하는게 좋다
        // MemberRepository로 데이터베이스에서 주어진 이메일 값으로 쿼리/묻기 위함.
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("해당 회원이 존재하지 않습니다."));

        // convertEntitytoDto 로 민감한 자료부분들 감추는 기능 할겸 클라이언트에서 요구되는 포맷으로 맞추기?
        return convertEntityToDto(member);
    }

    // 회원 정보 수정
    public boolean modifyMember(MemberReqDto memberReqDto) {
        try {
            //이메일로 검색하고
            Member member = memberRepository.findByEmail(memberReqDto.getEmail())
                    .orElseThrow(()-> new RuntimeException("해당 회원이 존재하지 않습니다."));
            //업데이트 행하기
            member.setName(memberReqDto.getName());
            member.setImgPath(memberReqDto.getImgPath());
            // 업데이트 내역 저장
            memberRepository.save(member);
            return true;
        } catch (Exception e) {
            log.error("회원 정보 수정 : {}", e.getMessage());
            return false;
        }
    }

    // 회원 삭제
    public boolean deleteMember(String email) {
        try {
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(()->new RuntimeException("해당 회원이 존재하지 않습니다."));
            memberRepository.delete(member);
            return true;
        } catch (Exception e) {
            log.error("회원 삭제에 실패하였습니다 : {}", e.getMessage());
            return false;
        }
    }

    private MemberResDto convertEntityToDtoWithBoard(Member member) {
        MemberResDto memberResDto = new MemberResDto();
        memberResDto.setEmail(member.getEmail());
        memberResDto.setName(member.getName());
        memberResDto.setRegDate(member.getRegDate());
        memberResDto.setImagePath(member.getImgPath());

        List<BoardResDto> boardResDtoList = new ArrayList<>();
        for (Board board : member.getBoards()) {
            BoardResDto boardResDto = new BoardResDto();
            boardResDto.setBoardId(board.getId());
            boardResDto.setTitle(board.getTitle());
            boardResDto.setImgPath(board.getImgPath());
            boardResDto.setRegDate(board.getRegDate());
            boardResDtoList.add(boardResDto);
        }
        memberResDto.setBoards(boardResDtoList);
        return memberResDto;

    }
}
