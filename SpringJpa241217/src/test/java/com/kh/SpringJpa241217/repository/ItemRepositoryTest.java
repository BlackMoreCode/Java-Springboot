package com.kh.SpringJpa241217.repository;

import com.kh.SpringJpa241217.constant.ItemSellStatus;
import com.kh.SpringJpa241217.entity.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository; // 생성자를 통해서 의존성 주입

    @Test   // 테스트임을 알림
    @DisplayName("상품 저장 테스트") //테스트 이름
    public void createItemTest() { // 아이템 저장 테스트용 함수
        for(int i = 1; i <= 10; i++) {
            Item item = new Item(); // 빈 객체 생성
            item.setItemNum("테스트 상품" + i);
            item.setPrice(1000 * i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            //상품 판매 상태를 짝수는 SELL, 홀수는 SOLD_OUT으로 변경
            if (i % 2 == 0) item.setItemSellStatus(ItemSellStatus.SELL);
            else item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNum(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item saveItem = itemRepository.save(item);
            log.info("Item 저장 : {}", saveItem);
        }
    }
    @Test
    @DisplayName("상품 조회 테스트")
    public void findByItemNumTest() {
        this.createItemTest(); // 10 개의 상품 생성
        List<Item> itemList = itemRepository.findByItemNum("테스트 상품5");
        for (Item item : itemList) {
            log.info("상품 조회 테스트 : {}", item);
        }
    }

    @Test
    @DisplayName("상품 명이나 상품 상세설명으로 조회하기 OR")
    public void findByItemNumOrItemDetailTest() {
        this.createItemTest();
        List<Item> itemList = itemRepository.findByItemNumOrItemDetail("테스트 상품9","테스트 상품 상세 설명2");
        for (Item item : itemList) {
            log.info("상품 명 or 상세 설명으로 조회 테스트 : {}", item);
        }
    }

    @Test
    @DisplayName("설정한 가격: 5000 미만의 상품 조회하기")
    public void findByPriceLessThanTest() {
        this.createItemTest();
        List<Item> itemList = itemRepository.findByPriceLessThan(5000);
        for (Item item : itemList) {
            log.info("설정한 가격: 5000 미만의 상품 조회하기 테스트 : {}", item);
        }

    }

    // 가격이 5000원 이상이고 판매 중인 상품만 조회하기
    @Test
    @DisplayName("가격이 5000원 이상이고 판매 중인 상품만 조회하기")
    public void findByPriceGreaterThanEqualAndItemSellStatusTest() {
        this.createItemTest();
        List<Item> itemList = itemRepository.findByPriceGreaterThanEqualAndItemSellStatus(5000, ItemSellStatus.SELL);
        for (Item item: itemList) {
            log.info("가격이 5000원 이상이고 판매 중인 상품 : {}", item);
        }
    }

    // 상품 가격에 대한 내림 차순 정렬하기
    @Test
    @DisplayName("상품 가격에 대한 내림 차순 정렬하기")
    public void findByPriceLessThanOrderByPriceDescTest() {
        this.createItemTest();
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(5000);
        for (Item item: itemList) {
            log.info("상품 가격에 대한 내림 차순 정렬하기 : {}", item);
        }
    }

    // 상품 이름에 특정 키워드가 포함된 상품 검색
    @Test
    @DisplayName("상품 이름에 특정 키워드가 포함된 상품 검색")
    public void findByItemNumContainingTest() {
        this.createItemTest();
        List<Item> itemList = itemRepository.findByItemNumContaining("상품1");
        for (Item item: itemList) {
            log.info("상품 이름에 특정 키워드가 포함된 상품 검색 : {}", item);
        }
    }

    //상품명과 가격이 일치하는 상품 검색 (AND)
    @Test
    @DisplayName("상품명과 가격이 일치하는 상품 검색 (AND)")
    public void findByItemNumAndItemDetailTest() {
        this.createItemTest();
        List<Item> itemList = itemRepository.findByItemNumAndItemDetail("상품1", "설명1");
        for (Item item: itemList) {
            log.info("상품명과 가격이 일치하는 상품 검색 (AND) : {}", item);
        }
    }

    //JPQL 테스트
    @Test
    @DisplayName("JPQL 상품 상세 정보 텍스트")
    public void findByJPQLTest() {
        this.createItemTest();
        List<Item> itemList = itemRepository.findByItemDetail("설명1");
        for (Item item: itemList) {
            log.info("JPQL Like 검색: {}", item);
        }
    }

    // native query 테스트
    @Test
    @DisplayName("NATIVE 상품 상세 정보 텍스트")
    public void findByNativeQueryTest() {
        this.createItemTest();
        List<Item> itemList = itemRepository.findByItemDetailNative("설명1");
        for(Item item : itemList) {
            log.info("Native Query Like 검색 {}", item);
        }
    }

}