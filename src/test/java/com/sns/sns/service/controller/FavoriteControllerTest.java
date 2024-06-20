package com.sns.sns.service.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sns.sns.service.domain.favorite.controller.FavoriteController;
import com.sns.sns.service.domain.favorite.dto.response.AddFavoriteResponse;
import com.sns.sns.service.domain.favorite.service.FavoriteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FavoriteService favoriteService;

    @Autowired
    private ObjectMapper objectMapper;

    // 좋아요 실패 케이스 작성
    // 1. 비회원일경우
    // 2. 이미 누른 좋아요 일 경우 다시 못누름

    @Test
    @WithMockUser
    @DisplayName("좋아요 클릭 성공 케이스")
    public void successFavorite() throws Exception {

        when(favoriteService.addFavorite(any(), any())).thenReturn(mock(AddFavoriteResponse.class));
        mockMvc.perform(post("/api/v1/favorite/board/1")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("좋아요 호출 케이스")
    public void getFavorite() throws Exception {

        when(favoriteService.addFavorite(any(), any())).thenReturn(mock(AddFavoriteResponse.class));
        mockMvc.perform(get("/api/v1/favorite/board/1")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("비회원 좋아요 실패 케이스")
    public void failAnonymousUserFavorite() throws Exception {

        when(favoriteService.addFavorite(any(), any())).thenReturn(mock(AddFavoriteResponse.class));
        mockMvc.perform(post("/api/v1/favorite/board/1")
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("좋아요 삭제 성공 케이스")
    public void deleteFavorite() throws Exception {

        when(favoriteService.addFavorite(any(), any())).thenReturn(mock(AddFavoriteResponse.class));
        mockMvc.perform(delete("/api/v1/favorite/board/1")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

}
