package com.kh.SpringJpa241217.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @ToString
@NoArgsConstructor
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long commentId; // comment_id

    @ManyToOne // comment 가 many, board가 one
    @JoinColumn
    private Board board; // 게시글에 대한 정보 없이 댓글이 존재할 수 없으니 게시글에 대한 정보를 넣어준다.

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length = 1000)
    private String content;

    private LocalDateTime regDate;
    @PrePersist // DB에 삽입하기 직전에 불러주는 콜백 함수.
    public void prePersist() {
        regDate = LocalDateTime.now();
    }

}
