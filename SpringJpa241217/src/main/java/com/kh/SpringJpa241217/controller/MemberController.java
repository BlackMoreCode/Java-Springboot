package com.kh.SpringJpa241217.controller;

import com.kh.SpringJpa241217.dto.MemberReqDto;
import com.kh.SpringJpa241217.dto.MemberResDto;
import com.kh.SpringJpa241217.entity.Member;
import com.kh.SpringJpa241217.service.MemberService;
import io.swagger.models.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "https://localhost:3000") // 이번까지만. 추후엔 뺀다
@RestController
@RequestMapping("/member") // 진입 경로
@RequiredArgsConstructor

//실습
// 1.POSTMAN으로 회원 존재여부 확인, 회원가입, 로그인
// 2.회원 전체 조회 및 회원 이메일 조회 만들기 (POSTMAN)
// 3. SWAGGER 등록 후 회원 존재여부 확인, 회원가입, 로그인, 회원 전체조회, 개별 회원 조회, 회원 삭제
// 4. MemberController : 회원 전체 조회, 개별 회원 조회, 회원정보 수정, 회원 삭제

public class MemberController {

    private final MemberService memberService;

    // 모든 멤버 조회
    @GetMapping("/list")
    public ResponseEntity<List<MemberResDto>> getMemberDetail() {
        List<MemberResDto> MemberResDto = memberService.getMemberList();
        return ResponseEntity.ok(MemberResDto);
    }


    // 특정 멤버 정보 조회
    // 요구하는 패러미터 = email --> 이 경우 http://localhost:8111/member/memberDetail?email=testaccount@gmail.com  이런식으로.
    @GetMapping("/detail")
    public ResponseEntity<MemberResDto> getMemberDetail(@RequestParam String email) {
        try {
            log.info("Fetching details for member with email: {}", email);
            // memberResDto를 호출하는 이유는 service에서 Member entity를 meemberResDto로 변환쳤기 때문
            // 이는 또한 비밀번호 등등을 숨겨서 전송해야할 때 쓰인다.
            MemberResDto memberResDto = memberService.getMemberDetail(email);
            return ResponseEntity.ok(memberResDto);
        } catch (RuntimeException e) {
            log.error("멤버 정보 불러오기 실패 :  {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // 회원 정보 수정 (PutMapping vs PostMapping 찾아보기)
    @PostMapping("/modify")
    public ResponseEntity<Boolean> modifyMember(@RequestBody MemberReqDto memberReqDto) {
        log.info("멤버 업데이트 시도 :  {}", memberReqDto);
        boolean isSuccess = memberService.modifyMember(memberReqDto);
        return ResponseEntity.ok(isSuccess);
    }


    // 멤버 삭제; param 쓰니까 http://localhost:8111/member/delete?email=blackmoreinvoker@gmail.com  이런식으로
    @PostMapping("/delete")
    public ResponseEntity<Boolean> deleteMember(@RequestParam  String email) {
        log.info("해당 이메일로 멤버 삭제 시도 : {}", email);
        boolean isSuccess = memberService.deleteMember(email);
        return ResponseEntity.ok(isSuccess);
    }

    // 멤버 삭제 강사님 버전
    @DeleteMapping("/{email}")
    public ResponseEntity<Boolean> memberDelete(@PathVariable String email) {
        boolean isSuccess = memberService.deleteMember(email);
        return ResponseEntity.ok(isSuccess);
    }




    //--------------------------------------------------//
    // 혼자 시도했을 때 방법들
    // 모든 멤버 조회 (수업에서의 예시). 컨트롤러에서 아무것도 하지 않는게 좋다 가능하면!
    // = 여기서 리스트 만들지 말라..

    // 모든 멤버 조회 내가 시도했던 것 (바꾸기 전 service랑 페어) 기존에 만든 것을 활용한 것
//    @GetMapping("/list")
//    public ResponseEntity<List<Member>> getAllMembers() {
//        log.info("Fetching all members");
//        List<Member> members = memberService.getAllMembers();
//        return ResponseEntity.ok(members);
//    }

    // 특정 멤버 정보 조회
    // 요구하는 패러미터 = email --> 이 경우 http://localhost:8111/member/memberDetail?email=testaccount@gmail.com  이런식으로.
//    @GetMapping("/memberDetail")
//    public ResponseEntity<Member> getMemberDetail(@RequestParam String email) {
//        try {
//            log.info("Fetching details for member with email: {}", email);
//            Member member = memberService.getMemberDetail(email);
//            return ResponseEntity.ok(member);
//        } catch (RuntimeException e) {
//            log.error("회원 정보 조회에 실패하였습니다: {}", e.getMessage());
//            return ResponseEntity.badRequest().build(); // 에러시 400 에러 반환
//        }
//    }
    // PathVariable 사용시 이렇게 될려나?
//    @GetMapping("/memberDetail/{email}")
//    public ResponseEntity<Member> getMemberDetail(@PathVariable String email) {
//        try {
//            log.info("Fetching details for member with email: {}", email);
//            Member member = memberService.getMemberDetail(email);
//            return ResponseEntity.ok(member);
//        } catch (RuntimeException e) {
//            log.error("회원 정보 조회에 실패하였습니다: {}", e.getMessage());
//            return ResponseEntity.badRequest().build(); // Return 400 Bad Request in case of error
//        }
//    }
}
