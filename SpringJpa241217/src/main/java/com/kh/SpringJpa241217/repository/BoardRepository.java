package com.kh.SpringJpa241217.repository;


import com.kh.SpringJpa241217.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByTitleContaining(String keyword); // 제목으로 검색

    //제목 혹은 내용으로 검색
    List<Board> findByTitleContainingOrContentContaining(String keyword, String contentKeyword);

    // 삭제 (제목으로?)
//    List<Board> deleteBoard(String keyword);

    // 수정 --> custom JPQL 사용을 할 시...?
    @Modifying
    @Query("UPDATE Board board SET board.content = :content WHERE board.id = :id")
    void updateContentById(@Param("id") Long id, @Param("content") String content);

}
