package com.kh.SpringJpa241217.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//게시글에 관한 엔티티; 회원과 게시글에 관한 연관 관계 (맵핑)
@Entity
@Table(name="board")
@Getter @Setter @ToString
@NoArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "board_id")
    private Long id;

    @Column(nullable = false)
    private String title;   // 게시글 제목

    @Lob
    @Column(length = 1000)
    private String content; // 게시글 내용

    private String imgPath; // 게시글 이미지 경로

    private LocalDateTime regDate;  // 게시글 등록 일자
    @PrePersist
    public void prePersist() {
        regDate = LocalDateTime.now();
    }
    @ManyToOne // 다 대 일 관계; 게시글 (다) 대 회원(일). 회원에 관한 참조 정보. 이미 만들어져있어야한다.
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board") // 주인이 아님을 의미; 즉, 객체를 참조만 함.
    private List<Comment> comments = new ArrayList<>(); // DB로 만들어지지 않는다. 참조만 한다.


}
