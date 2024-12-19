package com.kh.SpringJpa241217.entity;

import com.kh.SpringJpa241217.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity // 해당 클래스가 엔티티임을 나타냄 (즉, DB의 테이블이 된다)
@Table(name = "item")
@Getter @Setter @ToString
public class Item {
    @Id // PK = Primary Key = 기본키 필드 지정
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;    // 상품 코드

    @Column(nullable = false, length = 50)
    private String itemNum; // 상품명..?

    @Column(nullable = false) // DB 제약중 NOT NULL이 이것에 해당
    private int price;

    @Column(nullable = false)
    private int stockNum;   // 재고 수량

    @Lob    // 대용량 데이터 맵핑
    @Column(nullable = false)
    private String itemDetail;  // 상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; // 상품판매상태

    private LocalDateTime regTime;
    private LocalDateTime updateTime;

}
