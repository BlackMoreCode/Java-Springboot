package com.kh.SpringJpa241217.service;

import com.kh.SpringJpa241217.dto.BoardReqDto;
import com.kh.SpringJpa241217.dto.BoardResDto;
import com.kh.SpringJpa241217.entity.Board;
import com.kh.SpringJpa241217.entity.Member;
import com.kh.SpringJpa241217.repository.BoardRepository;
import com.kh.SpringJpa241217.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Required;
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
    @Transactional // 여러개의 쿼리가 날라가면 하나의 로직일 시 묶어야한다.
    public boolean saveBoard(BoardReqDto boardReqDto) {
        try {
            //회원 정보가 있는지 확인해야한다. 회원정보에 대한 객체를 넣어줘야한다.
            //프론트 엔드 전달한 이메일로 회원 정보를 가져온다.
            Member member = memberRepository.findByEmail(boardReqDto.getEmail())
                    .orElseThrow(()->new RuntimeException("해당 회원이 존재하지 않습니다."));

            Board board = new Board();
            board.setTitle(boardReqDto.getTitle());
            board.setContent(boardReqDto.getContent());
            board.setImgPath(boardReqDto.getImgPath());
            board.setMember(member);
            boardRepository.save(board);  // Insert
            return true;
        } catch (Exception e) {
            log.error("게시글 등록 실패 : {}", e.getMessage());
            return false;
        }
    }
    // 게시글 상세 조회 --> 프론트가 고유해야할 값인 id를 가지고 백에 상세정보를 요구하는 셈. BoardResDto 관련
    public BoardResDto findByBoardId(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("해당 게시글이 존재하지 않습니다."));

//        BoardResDto boardResDto = new BoardResDto();
//        boardResDto.setBoardId(board.getId());
//        boardResDto.setTitle(board.getTitle());
//        boardResDto.setContent(board.getContent());
//        boardResDto.setImgPath(board.getImgPath());
//        boardResDto.setRegDate(board.getRegDate());
//        boardResDto.setEmail(board.getMember().getEmail());
        //밑에서 따로 처리해서 이렇게 불러옴으로서 재활용 가능
        return convertEntityToDto(board);
    }

    // 게시글 전체 조회
    public List<BoardResDto> findAllBoard() {
        List<Board> boards = boardRepository.findAll(); // DB에 있는 모든 게시글 끌고오기
        List<BoardResDto> boardResDtoList = new ArrayList<>();
        for (Board board : boards) {
            // boardResDto라는 빈 객체를 만들어서 하나하나 넣어주기
            // convertEntityToDto를 통해서 BoardResDto를 반환 받아서 List에 주기
            boardResDtoList.add(convertEntityToDto(board));
        }
        return boardResDtoList;
    }

    // 게시글 검색 (제목으로)
    public List<BoardResDto> searchBoard(String keyword) {
        List<Board> boards = boardRepository.findByTitleContaining(keyword);
        List<BoardResDto> boardResDtoList = new ArrayList<>();
        for (Board board : boards) {
            // boardResDto라는 빈 객체를 만들어서 하나하나 넣어주기
            // convertEntityToDto를 통해서 BoardResDto를 반환 받아서 List에 주기
            boardResDtoList.add(convertEntityToDto(board));
        }
        return boardResDtoList;
    }

    //게시글 검색 (제목 과 내용)
    public List<BoardResDto> searchBoardTitleOrContent(String title, String content) {
        List<Board> boards = boardRepository.findByTitleContainingOrContentContaining(title, content);
        List<BoardResDto> boardResDtoList = new ArrayList<>();
        for (Board board : boards) {
            boardResDtoList.add(convertEntityToDto(board));
        }
        return boardResDtoList;
    }


    // 게시글 페이징
    public List<BoardResDto> pagingBoardList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Board> boards = boardRepository.findAll(pageable).getContent();
        List<BoardResDto> boardResDtoList = new ArrayList<>();
        for (Board board : boards) {
            // boardResDto라는 빈 객체를 만들어서 하나하나 넣어주기
            // convertEntityToDto를 통해서 BoardResDto를 반환 받아서 List에 주기
            boardResDtoList.add(convertEntityToDto(board));
        }
        return boardResDtoList;

    }

    //게시글 페이지 주 조회
    public int getBoardPageCount(int page, int size) {
        ProcessHandle PageReqiests;
        PageRequest pageRequest = PageRequest.of(page,size);
        return boardRepository.findAll(pageRequest).getTotalPages();
    }

    //게시글 삭제
    public boolean deleteBoard(Long id, String email) {
        try {
            Board board = boardRepository.findById(id)
                    .orElseThrow(()-> new RuntimeException("해당 게시글이 존재하지 않습니다"));
            if (board.getMember().getEmail().equals(email)) {
                boardRepository.delete(board);
                return true;
            } else {
                log.error("게시글은 작성자만 지울 수 있습니다.");
                return false;
            }
        } catch (Exception e) {
            log.error("삭제도중 에러 발생 : {}", e.getMessage());
            return false;
        }
    }

    //게시글 수정 (엔티티 기반 업데이트)
    public boolean updateBoardContent(Long id, BoardReqDto boardReqDto) {
        try {
            Board board = boardRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다"));

            Member member = memberRepository.findByEmail(boardReqDto.getEmail())
                    .orElseThrow(()->new RuntimeException("회원이 존재하지 않습니다"));

            if (board.getMember().getEmail().equals(boardReqDto.getEmail())) {
                board.setTitle(board.getTitle());
                board.setContent(board.getContent());
                board.setImgPath(board.getImgPath());
                board.setMember(member);
                boardRepository.save(board);
                return true;
            } else {
                log.error("게시글은 작성자만 수정할 수 있습니다.");
                return false;
            }
        } catch (Exception e) {
            log.error("수정 도중 에러 발생 : {}", e.getMessage());
            return false;
        }
    }

    // 게시글 수정 (만약 커스텀 JPQL 사용시)
    public void updateBoardContentJPQL(Long id, String newContent) {
        boardRepository.updateContentById(id, newContent);
    }



    private BoardResDto convertEntityToDto(Board board) {
        BoardResDto boardResDto = new BoardResDto();
        boardResDto.setBoardId(board.getId());
        boardResDto.setTitle(board.getTitle());
        boardResDto.setContent(board.getTitle());
        boardResDto.setImgPath(board.getImgPath());
        boardResDto.setEmail(board.getMember().getEmail());
        boardResDto.setRegDate(board.getRegDate());
        return boardResDto;
    }

}
