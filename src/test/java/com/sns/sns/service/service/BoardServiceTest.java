package com.sns.sns.service.service;


import com.sns.sns.service.data.BoardData;
import com.sns.sns.service.domain.board.dto.request.BoardRequest;
import com.sns.sns.service.domain.board.dto.request.BoardUpdateRequest;
import com.sns.sns.service.domain.board.dto.response.BoardUpdateResponse;
import com.sns.sns.service.domain.board.model.BoardEntity;
import com.sns.sns.service.domain.board.repository.BoardRepository;
import com.sns.sns.service.domain.board.service.BoardService;
import com.sns.sns.service.domain.exception.BasicException;
import com.sns.sns.service.domain.exception.ErrorCode;
import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.sns.sns.service.data.BoardData.boardRequestData;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BoardServiceTest {

    //게시물 서비스 에러사항
    @Autowired
    private BoardService boardService;
    @MockBean
    private BoardRepository boardRepository;
    @MockBean
    private MemberRepository memberRepository;

    @Test
    @DisplayName("게시물 작성 성공")
    public void successToWrite(){

        String title = "title";
        String content = "content";

        BoardRequest boardRequest = new BoardRequest(title, content);
        Member member = new Member("test", "password", "email");

        when(memberRepository.findByUserName(anyString())).thenReturn(Optional.of(member));
        when(boardRepository.save(any())).thenReturn(mock(BoardEntity.class));

        Assertions.assertDoesNotThrow(()->boardService.writeBoard(boardRequest,member));
    }


    @Test
    @DisplayName("게시물 작성 실패")
    public void failToWrite(){

        String title = "title";
        String content = "content";

        BoardRequest boardRequest = new BoardRequest(title, content);
        Member member = new Member("test", "password", "email");

        when(memberRepository.findByUserName(anyString())).thenReturn(Optional.empty());
        when(boardRepository.save(any())).thenReturn(mock(BoardEntity.class));

        BasicException exception = Assertions.assertThrows(BasicException.class,()->boardService.writeBoard(boardRequest,member));

        Assertions.assertEquals(exception.getErrorCode(),ErrorCode.NOT_EXIST_MEMBER);
    }

    @Test
    @DisplayName("게시물 업데이트 성공")
    public void updateBoard(){

        String title = "title";
        String content = "content";

        String updateTitle = "updatedTitle";
        String updateContent = "updatedContent";

        BoardRequest boardRequest = new BoardRequest(title, content);
        Member member = new Member("test", "password", "email");

        BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest(updateTitle, updateContent);

        when(memberRepository.findByUserName(anyString())).thenReturn(Optional.of(member));
        when(boardRepository.findById(anyLong())).thenReturn(Optional.of(new BoardEntity(boardRequest.title(), boardRequest.contents(), member)));

        BoardUpdateResponse boardUpdateResponse = boardService.updateBoard(anyLong(), boardUpdateRequest, member);

        Assertions.assertDoesNotThrow(()->boardUpdateResponse);

        Assertions.assertEquals(boardUpdateResponse.title() , updateTitle);
        Assertions.assertEquals(boardUpdateResponse.content() , updateContent);

    }

    @Test
    @DisplayName("같인 사용자 게시물 업데이트 성공")
    public void sameUserUpdateBoard(){

        String title = "title";
        String content = "content";
        String username = "test";
        String password = "password";

        BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest(title, content);
        Member member = new Member(username, password, "email");

        BoardEntity boardEntity = BoardData.updateBoard(boardUpdateRequest, member);

        when(memberRepository.findByUserName(member.getUsername())).thenReturn(Optional.ofNullable(boardEntity.getMember()));
        when(boardRepository.findById(anyLong())).thenReturn(Optional.of(boardEntity));


        Assertions.assertDoesNotThrow(()-> boardService.updateBoard(1L,boardUpdateRequest,member));

    }


    @Test
    @DisplayName("게시물이 존재하지 않는 경우")
    public void boardDoesntExist(){

        String title = "title";
        String content = "content";
        String username = "test";
        String password = "password";

        BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest(title, content);
        Member member = new Member(username, password, "email");

        BoardEntity boardEntity = BoardData.createNewBoard(boardRequestData(), member);

        when(memberRepository.findByUserName(member.getUsername())).thenReturn(Optional.of(member));
        when(boardRepository.findById(anyLong())).thenReturn(Optional.empty());

        BasicException error = Assertions.assertThrows(BasicException.class,()-> boardService.updateBoard(1L,boardUpdateRequest,member));

        Assertions.assertEquals(error.getErrorCode(),ErrorCode.NOT_EXIST_BOARD);
    }

    @Test
    @DisplayName("게시물에 권한이 없는경우")
    public void unathurizedBoard(){

        String title = "title";
        String content = "content";
        String username = "test";
        String password = "password";

        BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest(title, content);
        Member member = new Member(username, password, "email");
        Member member2 = new Member(username + '2', password, "email");

        BoardEntity boardEntity = BoardData.createNewBoard(boardRequestData(), member);

        when(memberRepository.findByUserName(member.getUsername())).thenReturn(Optional.of(member2));
        when(boardRepository.findById(anyLong())).thenReturn(Optional.of(boardEntity));

        BasicException error = Assertions.assertThrows(BasicException.class,()-> boardService.updateBoard(1L,boardUpdateRequest,member));

        Assertions.assertEquals(error.getErrorCode(),ErrorCode.INVALID_PERMISSION);

    }


    @Test
    @DisplayName("게시물이 성공적으로 삭제된 경우")
    public void successToDeleteBoard(){
        //멤버와
        //보더를 일단 구해옴
//        멤버가 해당 게시물을 삭제하려는지 확인해야함.
        String userName = "test";
        String password = "password";
        Member member = new Member(userName, password, "email");
        BoardEntity boardEntity = new BoardEntity("title", "contents", member);

        when(memberRepository.findByUserName(member.getUsername())).thenReturn(Optional.of(member));
        when(boardRepository.findById(any())).thenReturn(Optional.of(boardEntity));

        Assertions.assertDoesNotThrow(()-> boardService.deleteBoard(1L, member));


    }

    @Test
    @DisplayName("권한이없어 게시물이 삭제되지 않은 경우 ")
    public void failToDeleteBoard(){

        String userName = "test";
        String password = "password";
        Member member = new Member(userName, password, "email");
        Member member2 = new Member(userName +"2", password, "email");
        BoardEntity boardEntity = new BoardEntity("title", "contents", member);

        when(memberRepository.findByUserName(member.getUsername())).thenReturn(Optional.of(member2));
        when(boardRepository.findById(any())).thenReturn(Optional.of(boardEntity));

        BasicException error =  Assertions.assertThrows(BasicException.class,()-> boardService.deleteBoard(1L, member));

        Assertions.assertEquals(error.getErrorCode(), ErrorCode.INVALID_PERMISSION);

    }

    @Test
    @DisplayName("게시물이 존재하지 않아 삭제되지 않는 경우")
    public void boardDoesntExistDelete(){

        String title = "title";
        String content = "content";
        String username = "test";
        String password = "password";

        Member member = new Member(username, password, "email");

        BoardEntity boardEntity = BoardData.createNewBoard(boardRequestData(), member);

        when(memberRepository.findByUserName(member.getUsername())).thenReturn(Optional.of(member));
        when(boardRepository.findById(anyLong())).thenReturn(Optional.empty());

        BasicException error = Assertions.assertThrows(BasicException.class,()-> boardService.deleteBoard(1L,member));

        Assertions.assertEquals(error.getErrorCode(),ErrorCode.NOT_EXIST_BOARD);
    }


    @Test
    @DisplayName("모든 게시물 가져올경우")
    public void getAllBoard(){
        Pageable pageable = mock(Pageable.class);

        BoardEntity board = new BoardEntity("title", "content", new Member("username", "password", "email"));
        when(boardRepository.findAll(pageable)).thenReturn(Page.empty());
        Assertions.assertDoesNotThrow(()-> boardService.getBoard(pageable));

    }

    @Test
    @DisplayName("사용자 게시물 가져올 경우 성공")
    public void getUserBoard(){
        Member member = new Member("username", "password", "email");
        Pageable pageable = mock(Pageable.class);

        BoardEntity board = new BoardEntity("title", "content", member);
        when(memberRepository.findByUserName(member.getUsername())).thenReturn(Optional.of(member));
        when(boardRepository.findAllByMember(any(),any())).thenReturn(Page.empty());
        Assertions.assertDoesNotThrow(()-> boardService.getUserBoard(pageable, member));
    }

    @Test
    @DisplayName("게시물 가져올때 사용자가 없을 경우실패")
    public void failedToGetNotExistUserBoard(){
        Member member = new Member("username", "password", "email");
        Pageable pageable = mock(Pageable.class);

        BoardEntity board = new BoardEntity("title", "content", member);
        when(memberRepository.findByUserName(member.getUsername())).thenReturn(Optional.empty());
        when(boardRepository.findAll()).thenReturn(List.of(board));
        BasicException e = Assertions.assertThrows(BasicException.class,()-> boardService.getUserBoard(pageable, member));

        Assertions.assertEquals(e.getErrorCode(),ErrorCode.NOT_EXIST_MEMBER);
    }

}