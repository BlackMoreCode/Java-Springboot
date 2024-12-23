package com.kh.SpringJpa241217.entity;



import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity // 해당 클래스가 entity임을 나타냄; 일반 클래스가 아니고 DB를 만드는 클래스
@Table(name="member") // 테이블 이름 지정, 테이블 이름은 소문자, 카멜 표기법은 스네이크 표기법으로 변경됨.
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString // 오버라이딩

public class Member {
    @Id // 해당 필드를 기본 키(Primary Key; PK) 로 지정
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.AUTO) // 기본키 생성 전략; JPA가 자동으로 생성전략을 정함.
    private Long id; // Primary Key

    @Column(nullable = false, length = 50) //email은 null이 오면 안된다라는 제약 조건.
    private String email;
    @Column(nullable = false, length = 50) //  Length 부분은 기본적으로 255지만 명시적으로 제약을 걸 수도 있다
    private String pwd;

    @Column(length = 50)
    private String name;

    @Column(name="reg_date") // 이름을 명시적으로 정해두고 싶다면 이런식으로 가능.
    private LocalDateTime regDate;
    @Column(name="image_path")
    private String imgPath;
    @PrePersist // JPA의 콜백 메서드로 엔티티가 저장되기 전에 실행; DB 데이터가 삽입되기 전에 자동 설정

    protected void onCreate() {
        this.regDate = LocalDateTime.now(); // 현재 시간을 등록 시간으로 넣어준다.
    }

    // 게시글 목록에 대한 OneToMany
    @OneToMany(mappedBy = "member")
    private List<Board> boards = new ArrayList<>();

}
