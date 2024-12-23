package com.kh.SpringJpa241217.repository;


import com.kh.SpringJpa241217.entity.Board;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByTitleContaining(String keyword); // 제목으로 검색

    //제목 혹은 내용으로 검색
    List<Board> findByTitleContainingOrContentContaining(String keyword, String contentKeyword);

    @EntityGraph(attributePaths = "comments") // 댓글까지 읽어올 수 있게 추가
    Optional<Board> findById(Long id);


    // 수정 --> custom JPQL 사용을 할 시...?
    @Modifying
    @Query("UPDATE Board board SET board.content = :content WHERE board.id = :id")
    void updateContentById(@Param("id") Long id, @Param("content") String content);

}
