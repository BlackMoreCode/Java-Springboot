package com.kh.SpringJpa241217.service;

import com.kh.SpringJpa241217.dto.BoardResDto;
import com.kh.SpringJpa241217.dto.CommentReqDto;
import com.kh.SpringJpa241217.dto.CommentResDto;
import com.kh.SpringJpa241217.entity.Board;
import com.kh.SpringJpa241217.entity.Comment;
import com.kh.SpringJpa241217.entity.Member;
import com.kh.SpringJpa241217.repository.BoardRepository;
import com.kh.SpringJpa241217.repository.CommentRepository;
import com.kh.SpringJpa241217.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j // log 메세지 출력
@Service // Spring 컨테이너에 등록
@RequiredArgsConstructor

public class CommentService {
    private final MemberRepository memberRepository; // member 정보를 가져 오기 위해서
    private final BoardRepository boardRepository; // 게시글 정보를 가져오기 위함.
    private final CommentRepository commentRepository; // 댓글 작성을 위함.

    // 댓글 등록
    @Transactional
    public boolean commentRegister(CommentReqDto commentReqDto) {
        try {
            Member member = memberRepository.findByEmail(commentReqDto.getEmail())
                    .orElseThrow(()-> new RuntimeException("회원이 존재하지 않습니다."));
            Board board = boardRepository.findById(commentReqDto.getBoardId())
                    .orElseThrow(()-> new RuntimeException("게시글이 존재하지 않습니다."));
            Comment comment = new Comment();
            comment.setContent(commentReqDto.getContent());
            comment.setMember(member);
            comment.setBoard(board);
            commentRepository.save(comment); // comment 객체를 저장해준다.
            return true;
        } catch (Exception e) {
            log.error("댓글 등록 실패 : {}", e.getMessage());
            return false;
        }
    }

    // 댓글 삭제 내 원래 시도
//    public boolean commentDelete(Long commentId, CommentReqDto commentReqDto) {
//        try {
//            // 댓글을 ID로 찾거나 아닐 경우 에러 던지기
//            Comment comment = commentRepository.findById(commentId)
//                    .orElseThrow(() -> new RuntimeException("해당 댓글이 존재하지 않습니다."));
//
//            // 댓글 작성자인지 email로 확인; 불일치시 false 반환.
//            if (!comment.getMember().getEmail().equals(commentReqDto.getEmail())) {
//                log.error("댓글 삭제 실패: 요청한 이메일이 댓글 작성자의 이메일과 일치하지 않습니다.");
//                return false;
//            }
//
//            commentRepository.delete(comment);
//            return true;
//
//        } catch (Exception e) {
//            log.error("댓글 삭제 실패: {}", e.getMessage());
//            return false;
//        }
//    }
    //댓글 삭제 강사님 예제
    public boolean commentDelete(Long commentId, String email) {
        try {
            // 댓글을 ID로 찾거나 아닐 경우 에러 던지기
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new RuntimeException("해당 댓글이 존재하지 않습니다."));
            if(comment.getMember().getEmail().equals(email)) {
                commentRepository.delete(comment);
                return true;
            } else {
                log.error("댓글은 작성자만 삭제가 가능합니다");
                return false;
            }
        } catch (Exception e) {
            log.error("댓글 삭제 실패: {}", e.getMessage());
            return false;
        }
    }


    // 댓글 목록 조회 내 시도
//    public List<CommentResDto> findAllComment() {
//        // DB에서 댓글들을 전부 fetch
//        List<Comment> comments = commentRepository.findAll();
//        // responseDTO를 담을 빈 리스트 생성
//        List<CommentResDto> commentResDtoList = new ArrayList<>();
//        for (Comment comment : comments) {
//            // 각 코멘트 엔티티들을 DTO로 변환 처리 해주고 리스트에 추가
//            commentResDtoList.add(convertEntityToDto(comment));
//        }
//        return commentResDtoList;


    //댓글 목록 조회 강사님 예제
    public List<CommentResDto> commentList(Long boardId) {
        try {
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(()-> new RuntimeException("해당 게시글이 존재하지 않습니다."));
            List<Comment> commentList = commentRepository.findByBoard(board); // 해당 게시글(보드)에 대한 댓글 목록을 가져 옴.
            List<CommentResDto> commentResDtoList = new ArrayList<>();
            for (Comment comment : commentList) {
                CommentResDto commentResDto = new CommentResDto();
                commentResDto.setBoardId(comment.getBoard().getId());
                commentResDto.setCommentId(comment.getCommentId());
                commentResDto.setEmail(comment.getMember().getEmail());
                commentResDto.setContent(comment.getContent());
                commentResDto.setRegDate(comment.getRegDate());
                commentResDtoList.add(commentResDto);
            }
            return commentResDtoList;

        } catch (Exception e) {
            log.error("댓글 목록 조회 실패 : {}", e.getMessage());
            return null;
        }
    }


    private CommentResDto convertEntityToDto(Comment comment) {
        CommentResDto commentResDto = new CommentResDto();
        commentResDto.setBoardId(comment.getBoard().getId()); // 보드 객체를 전체로 넘겨주지 말고, ID만을 빼서 넘겨줘야 에러가 안난다..
        commentResDto.setCommentId(comment.getCommentId());
        commentResDto.setContent(comment.getContent());
        commentResDto.setEmail(comment.getMember().getEmail());
        commentResDto.setRegDate(comment.getRegDate());
        return commentResDto;
    }

}
