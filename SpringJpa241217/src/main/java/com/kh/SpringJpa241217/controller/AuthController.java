package com.kh.SpringJpa241217.controller;

import com.kh.SpringJpa241217.dto.LogInReqDto;
import com.kh.SpringJpa241217.dto.MemberReqDto;
import com.kh.SpringJpa241217.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "https://localhost:3000") // 이번까지만. 추후엔 뺀다
@RestController
@RequestMapping("/auth") // 진입 경로
@RequiredArgsConstructor

public class AuthController {
    private final AuthService authService; // 의존성 주입

    // 회원 가입 여부 확인
    @GetMapping("/exist/{email}")
    public ResponseEntity<Boolean> isMember(@PathVariable String email) {
        boolean isTrue = authService.isMember(email);
        return ResponseEntity.ok(!isTrue); // 존재하면 가입되면 안되니까 !isTrue 처리
    }

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<Boolean> signUp(@RequestBody MemberReqDto memberReqDto) {
        boolean isSuccess = authService.signUp(memberReqDto);
        return ResponseEntity.ok(isSuccess);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@RequestBody LogInReqDto logInReqDto) {
        boolean isSuccess = authService.login(logInReqDto);
        return ResponseEntity.ok(isSuccess);
    }
}
