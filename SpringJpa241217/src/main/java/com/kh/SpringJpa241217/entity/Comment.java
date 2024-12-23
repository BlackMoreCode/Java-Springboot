package com.kh.SpringJpa241217.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @ToString(exclude = {"board", "member"}) // 문자열로 변환할때 객체 부분은 제외한다.
@NoArgsConstructor
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long commentId; // comment_id

    @ManyToOne // comment 가 many, board가 one
    @JoinColumn(name = "board_id") // 참조키는 해당 객체의 기본키여야 한다.
    private Board board; // 게시글에 대한 정보 없이 댓글이 존재할 수 없으니 게시글에 대한 정보를 넣어준다.

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length = 1000)
    private String content;

    @Column(nullable = false, updatable = false)
    private LocalDateTime regDate;
    @PrePersist // DB에 삽입하기 직전에 불러주는 콜백 함수.
    public void prePersist() {
        regDate = LocalDateTime.now();
    }

}
