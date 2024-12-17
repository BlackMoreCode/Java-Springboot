package com.kh.SpringJpa241217.repository;

import com.kh.SpringJpa241217.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 기본적인 CRUD 기능은 이미 만들어져있다.
    boolean existsByEmail(String email); // SELECT * FROM MEMBER WHERE email = "";
    Optional<Member> findByEmail(String email);
    Optional<Member> findByPwd(String pwd); // findByPw 라고 써버리면 죽어버린다!! 카멜 표기도 정확히 지켜줘야한다!!
    Optional<Member> findByEmailAndPwd(String email, String pwd);

}
