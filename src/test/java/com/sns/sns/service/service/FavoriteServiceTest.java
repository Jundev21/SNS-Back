package com.sns.sns.service.service;


import com.sns.sns.service.domain.board.model.BoardEntity;
import com.sns.sns.service.domain.board.repository.BoardRepository;
import com.sns.sns.service.domain.exception.BasicException;
import com.sns.sns.service.domain.exception.ErrorCode;
import com.sns.sns.service.domain.favorite.dto.response.AddFavoriteResponse;
import com.sns.sns.service.domain.favorite.model.FavoriteEntity;
import com.sns.sns.service.domain.favorite.repository.FavoriteRepository;
import com.sns.sns.service.domain.favorite.service.FavoriteService;
import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class FavoriteServiceTest {

    @Autowired
    private FavoriteService favoriteService;

    @MockBean
    private FavoriteRepository favoriteRepository;

    @MockBean
    private BoardRepository boardRepository;

    @MockBean
    private MemberRepository memberRepository;



    @Test
    @DisplayName("좋아요 클릭 성공 케이스")
    public void successFavorite() throws Exception {

        // 좋아요 성공케이스 작성 흐룸
        // 1. 사용자가 있는지 확인 - 목데이터
        // 2. 해당 게시물이 있는지 확인 - 목데이터
        // 3. 좋아요를 이미 눌렀는지 확인 (만약 안눌렀을시 좋아요 추가 성공) - 서비스 로직

        Member member = new Member("test", "test", "email");

        when(memberRepository.findByUserName(any()))
                .thenReturn(Optional.ofNullable(mock(Member.class)));

        when(boardRepository.findById(any()))
                .thenReturn(Optional.of(mock(BoardEntity.class)));
        when(favoriteRepository
                .findFavoriteEntityByBoardEntityAndMember(any(), any())
        ).thenReturn(Optional.ofNullable(mock(FavoriteEntity.class)));

        Assertions.assertDoesNotThrow(()->favoriteService.addFavorite(1L, member));
    }

    @Test
    @DisplayName("좋아요 호출 케이스")
    public void getFavorite() throws Exception {

        //게시물에 좋아요가 얼마나 있는지 게시물 탐색
        when(boardRepository.findById(any()))
                .thenReturn(Optional.of(mock(BoardEntity.class)));

        when(favoriteRepository.findByBoardEntity(any()))
                .thenReturn(Optional.of(mock(FavoriteEntity.class)));

//        Assertions.assertDoesNotThrow(()-> favoriteService.getFavorite(any()));
    }

    @Test
    @WithMockUser
    @DisplayName("좋아요 중복 실패 케이스")
    public void duplicateFavorite(){

        Member member = new Member("test", "test", "email");

        when(memberRepository.findByUserName(any()))
                .thenReturn(Optional.ofNullable(mock(Member.class)));

        when(boardRepository.findById(any()))
                .thenReturn(Optional.of(mock(BoardEntity.class)));
        when(favoriteRepository
                .findFavoriteEntityByBoardEntityAndMember(any(), any())
        ).thenReturn(Optional.ofNullable(mock(FavoriteEntity.class)));

        BasicException e = Assertions.assertThrows(BasicException.class,()->favoriteService.addFavorite(1L, member));
        Assertions.assertEquals(e.getErrorCode(), ErrorCode.ALREADY_EXIST_FAVORITE);

    }


    @Test
    @WithMockUser
    @DisplayName("좋아요 삭제 성공 케이스")
    public void deleteFavorite(){

        Member member = new Member("test", "test", "email");

        when(memberRepository.findByUserName(any()))
                .thenReturn(Optional.ofNullable(mock(Member.class)));

        when(boardRepository.findById(any()))
                .thenReturn(Optional.of(mock(BoardEntity.class)));
        when(favoriteRepository
                .findFavoriteEntityByBoardEntityAndMember(any(), any())
        ).thenReturn(Optional.ofNullable(mock(FavoriteEntity.class)));

        Assertions.assertDoesNotThrow(()->favoriteService.deleteFavorite(1L, member));


    }

    @Test
    @DisplayName("좋아요 권한이 없어 삭제 실패 케이스")
    public void deleteFailFavorite(){

        Member member = new Member("test", "test", "email");

        when(memberRepository.findByUserName(any()))
                .thenReturn(Optional.ofNullable(mock(Member.class)));

        when(boardRepository.findById(any()))
                .thenReturn(Optional.of(mock(BoardEntity.class)));
        when(favoriteRepository
                .findFavoriteEntityByBoardEntityAndMember(any(), any())
        ).thenReturn(Optional.empty());

        BasicException e = Assertions.assertThrows(BasicException.class,()->favoriteService.deleteFavorite(1L, member));
        Assertions.assertEquals(e.getErrorCode(), ErrorCode.INVALID_PERMISSION_FAVORITE);


    }



}
