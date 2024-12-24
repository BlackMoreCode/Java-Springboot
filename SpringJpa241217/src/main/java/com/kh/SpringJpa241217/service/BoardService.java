package com.kh.SpringJpa241217.service;

import com.kh.SpringJpa241217.dto.*;
import com.kh.SpringJpa241217.entity.Board;
import com.kh.SpringJpa241217.entity.Comment;
import com.kh.SpringJpa241217.entity.Member;
import com.kh.SpringJpa241217.repository.BoardRepository;
import com.kh.SpringJpa241217.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    // 게시글 등록
    @Transactional
    public boolean saveBoard(BoardReqDto boardReqDto) {
        try {
            // 프론트앤드에서 전달한 이메일로 회원 정보를 가져 옴
            Member member = memberRepository.findByEmail(boardReqDto.getEmail())
                    .orElseThrow(()->new RuntimeException("해당 회원이 존재하지 않습니다."));

            Board board = new Board();
            board.setTitle(boardReqDto.getTitle());
            board.setContent(boardReqDto.getContent());
            board.setImgPath(boardReqDto.getImgPath());
            board.setMember(member);
            boardRepository.save(board);// insert
            return true;
            } catch (Exception e) {
                log.error("게시글 등록 실패 : {}", e.getMessage());
                return false;
            }
    }
    // 게시글 상세 조회
    public BoardResDto findByBoardId(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(()->new RuntimeException("해당 게시글이 존재하지 않습니다."));


        return convertEntityToDto(board);
    }
    // 게시글 전체 조회
    public List<BoardResDto> findAllBoard() {
        List<Board> boards = boardRepository.findAll();
        List<BoardResDto> boardResDtoList = new ArrayList<>();
        for(Board board : boards) {
            // convertEntityToDto를 통해서 BoardResDto를 반환 받아서 List에 add
            boardResDtoList.add(convertEntityToDtoWithoutComment(board));
        }
        return boardResDtoList;
    }
    // 게시글 검색
    public List<BoardResDto> searchBoardByTitle(String keyword) {
        List<Board> boards = boardRepository.findByTitleContaining(keyword);
        List<BoardResDto> boardResDtoList = new ArrayList<>();
        for(Board board : boards) {
            // convertEntityToDto를 통해서 BoardResDto를 반환 받아서 List에 add
            boardResDtoList.add(convertEntityToDtoWithoutComment(board));
        }
        return boardResDtoList;
    }
    // 게시글 페이지 수 조회
    public int getBoardPageCount(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return boardRepository.findAll(pageRequest).getTotalPages();
    }

    // 게시글 페이징
    public List<BoardResDto> pagingBoardList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Board> boards = boardRepository.findAll(pageable).getContent();
        List<BoardResDto> boardResDtoList = new ArrayList<>();
        for(Board board : boards) {
            // convertEntityToDto를 통해서 BoardResDto를 반환 받아서 List에 add
            boardResDtoList.add(convertEntityToDtoWithoutComment(board));
        }
        return boardResDtoList;
    }

    // 게시글 삭제
    public boolean deleteBoard (Long id, String email) {
        try {
            Board board = boardRepository.findById(id)
                            .orElseThrow(()->new RuntimeException("해당 게시글이 존재하지 않습니다."));
            if (board.getMember().getEmail().equals(email)) {
                boardRepository.delete(board);
                return true;
            } else {
                log.error("게시글은 작성자만 삭제 가능합니다.");
                return false;
            }
        } catch (Exception e) {
            log.error("게시글 삭제 실패 : {}", e.getMessage());
            return false;
        }

    }
    // 게시글 수정
    @Transactional
    public boolean updateBoard(BoardReqDto boardReqDto, Long boardId) {
        try {
            // 프론트앤드에서 전달한 이메일로 회원 정보를 가져 옴
            Member member = memberRepository.findByEmail(boardReqDto.getEmail())
                    .orElseThrow(()->new RuntimeException("해당 회원이 존재하지 않습니다."));
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(()->new RuntimeException("게시글을 찾을 수 없습니다."));
            if (board.getMember().getEmail().equals(boardReqDto.getEmail())) {
                board.setTitle(boardReqDto.getTitle());
                board.setContent(boardReqDto.getContent());
                board.setImgPath(boardReqDto.getImgPath());

            boardRepository.save(board);// update
            return true;
            } else {
                log.error("게시글은 작성자만 수정 할 수있습니다.");
                return false;
            }
        } catch (Exception e) {
            log.error("게시글 수정 실패 : {}", e.getMessage());
            return false;
        }
    }

    // 게시글 검색 (제목과 내용)
    public List<BoardResDto> searchBoardByTitleOrContent(String title, String content) {
        List<Board> boards = boardRepository.findByTitleContainingOrContentContaining(title, content);
        List<BoardResDto> boardResDtoList = new ArrayList<>();
        for(Board board : boards) {
            // convertEntityToDto를 통해서 BoardResDto를 반환 받아서 List에 add
            boardResDtoList.add(convertEntityToDto(board));
        }
        return boardResDtoList;
    }

    // 댓글 목록 조회
    public List<CommentResDto> commentList(Long boardId) {
        try {
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(()->new RuntimeException("해당 게시글이 없습니다."));
            List<CommentResDto> commentResDtoList = new ArrayList<>();
            for(Comment comment : board.getComments()) {    //Board.java에서 comments 사용
                CommentResDto commentResDto = new CommentResDto();
                commentResDto.setEmail(comment.getMember().getEmail());
                commentResDto.setBoardId(comment.getBoard().getId());
                commentResDto.setCommentId(comment.getCommentId());
                commentResDto.setContent(comment.getContent());
                commentResDto.setRegDate(comment.getRegDate());
                commentResDtoList.add(commentResDto);
            }
            return commentResDtoList;

        } catch (Exception e) {
            log.error("게시글에 대한 댓글 조회 실패 : {}", e.getMessage());
            return null;
        }
    }

    // 12-24
    // 게시글 엔티티에서 접근하여 코맨트를 추가
    @Transactional // 여러 쿼리문을 돌릴때 그 작업중 하나라도 실패하면 롤백
    public boolean addComment(Long boardId, CommentReqDto commentReqDto) {
        try {
            // id로 board 객체 가져오기
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(()->new RuntimeException("게시글이 존재하지 않습니다."));
            // email로 member 객체 가져오기
            Member member = memberRepository.findByEmail(commentReqDto.getEmail())
                    .orElseThrow(()->new RuntimeException("회원 정보가 존재하지 않습니다."));
            // Dto-> entity로 변환
            Comment comment =new Comment();
            comment.setContent(commentReqDto.getContent());
            comment.setBoard(board);
            comment.setMember(member);
            board.addComment(comment);
            boardRepository.save(board);
            return true;
        } catch (Exception e) {
         log.error("댓글 추가 실패 : {}",e.getMessage());
         return false;
        }
    }

    // 12-24
    @Transactional
    public boolean removeComment(Long boardId, Long commentId) {
        try {
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(()->new RuntimeException("게시글이 존재하지 않습니다."));
            Comment targetComment = null;   // 지울 댓글에 대한 변수 생성
            for(Comment comment : board.getComments()) {
                if(comment.getCommentId().equals(commentId)){
                    targetComment = comment;
                    break;
                }
            }
            if (targetComment == null) {
                log.error("해당 댓글이 존재하지 않습니다.");
                return false;
            }
            board.removeComment(targetComment);
            boardRepository.save(board);
            return true;

        } catch (Exception e){
            log.error("댓글 제거 실패 : {}",e.getMessage());
            return false;
        }

    };

    private BoardResDto convertEntityToDto(Board board) {
        BoardResDto boardResDto = new BoardResDto();
        boardResDto.setBoardId(board.getId());
        boardResDto.setTitle(board.getTitle());
        boardResDto.setContent(board.getContent());
        boardResDto.setImgPath(board.getImgPath());
        boardResDto.setEmail(board.getMember().getEmail());
        boardResDto.setRegDate(board.getRegDate());

        List<CommentResDto> commentResDtoList = new ArrayList<>();
        for(Comment comment : board.getComments()) {
            CommentResDto commentResDto = new CommentResDto();
            commentResDto.setBoardId(comment.getBoard().getId());
            commentResDto.setCommentId(comment.getCommentId());
            commentResDto.setEmail(comment.getMember().getEmail());
            commentResDto.setContent(comment.getContent());
            commentResDto.setRegDate(comment.getRegDate());
            commentResDtoList.add(commentResDto);
        }
        boardResDto.setComments(commentResDtoList);
        return boardResDto;
    }

    // 댓글 제외 Dto
    private BoardResDto convertEntityToDtoWithoutComment(Board board) {
        BoardResDto boardResDto = new BoardResDto();
        boardResDto.setBoardId(board.getId());
        boardResDto.setTitle(board.getTitle());
        boardResDto.setContent(board.getContent());
        boardResDto.setImgPath(board.getImgPath());
        boardResDto.setEmail(board.getMember().getEmail());
        boardResDto.setRegDate(board.getRegDate());
        boardResDto.setComments(new ArrayList<>());
        return boardResDto;
    }
}
