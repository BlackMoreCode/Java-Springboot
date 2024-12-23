package com.kh.SpringJpa241217.repository;

import com.kh.SpringJpa241217.entity.Board;
import com.kh.SpringJpa241217.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoard(Board board);
}
