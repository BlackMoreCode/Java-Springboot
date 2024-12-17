package com.kh.SpringJpa241217.entity;



import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="member")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString

public class Member {
    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // Primary Key

    @Column(nullable = false) //email은 null이 오면 안된다
    private String email;
    @Column(nullable = false)
    private String pwd;

    private String name;
    private LocalDateTime regDate;
    private String imgPath;
}
