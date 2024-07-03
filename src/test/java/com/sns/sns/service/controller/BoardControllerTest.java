package com.sns.sns.service.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sns.sns.service.common.exception.BasicException;
import com.sns.sns.service.common.exception.ErrorCode;
import com.sns.sns.service.domain.board.dto.request.BoardRequest;
import com.sns.sns.service.domain.board.dto.request.BoardUpdateRequest;
import com.sns.sns.service.domain.board.dto.response.BoardUpdateResponse;
import com.sns.sns.service.domain.board.service.BoardService;
import com.sns.sns.service.domain.member.dto.response.BasicUserInfoResponse;
import com.sns.sns.service.domain.member.model.entity.Member;

@SpringBootTest
@AutoConfigureMockMvc
public class BoardControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BoardService boardService;

	@Autowired
	private ObjectMapper objectMapper;

	// 게시물 작성 에러사항
	// 게시물 작성 성공
	// 게시물 작성 실패
	//  비회원 접근

	@Test
	@WithMockUser
	@DisplayName("게시물 작성 성공")
	public void successToWrite() throws Exception {

		String title = "title";
		String content = "content";

		BoardRequest boardRequest = new BoardRequest(title, content);
		mockMvc.perform(post("/api/v1/board")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(boardRequest)))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@WithAnonymousUser
	@DisplayName("비회원이 게시물을 작성하려는 경우")
	public void unAuthurizedToWrite() throws Exception {
		String title = "title";
		String content = "content";

		BoardRequest boardRequest = new BoardRequest(title, content);
		mockMvc.perform(post("/api/v1/board")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(boardRequest)))
			.andDo(print())
			.andExpect(status().isUnauthorized());
	}

	//==================수정=========================
	@Test
	@WithMockUser
	@DisplayName("게시물 수정")
	public void updateBoard() throws Exception {

		String title = "title";
		String content = "content";

		BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest(title, content);

		when(boardService.updateBoard(1L, boardUpdateRequest, mock(Member.class)))
			.thenReturn(new BoardUpdateResponse(
				1L,
				boardUpdateRequest.title(),
				boardUpdateRequest.content(),
				new BasicUserInfoResponse(1L, "test", 1L),
				LocalDateTime.now(),
				LocalDateTime.now()
			));

		mockMvc.perform(put("/api/v1/board/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(boardUpdateRequest)))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@WithAnonymousUser
	@DisplayName("로그인을 하지 않고 게시물을 수정했을시")
	public void anonymousUserBoard() throws Exception {

		String title = "title";
		String content = "content";

		BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest(title, content);

		when(boardService.updateBoard(1L, boardUpdateRequest, mock(Member.class)))
			.thenReturn(new BoardUpdateResponse(
					1L,
					boardUpdateRequest.title(),
					boardUpdateRequest.content(),
					new BasicUserInfoResponse(1L, "test", 0L),
					LocalDateTime.now(),
					LocalDateTime.now()

				)
			);

		mockMvc.perform(put("/api/v1/board/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(boardUpdateRequest)))
			.andDo(print())
			.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser
	@DisplayName("게시물 수정시 로그인 한 회원이 아닌경우")
	public void otherUserBoard() throws Exception {

		String title = "title";
		String content = "content";

		BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest(title, content);

		when(boardService.updateBoard(any(), any(), any()))
			.thenThrow(new BasicException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMsg()));

		mockMvc.perform(put("/api/v1/board/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(boardUpdateRequest)))
			.andDo(print())
			.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser
	@DisplayName("수정하려는 게시물 없는 경우")
	public void notExistBoard() throws Exception {

		String title = "title";
		String content = "content";

		BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest(title, content);

		when(boardService.updateBoard(any(), any(), any()))
			.thenThrow(new BasicException(ErrorCode.NOT_EXIST_BOARD, ErrorCode.NOT_EXIST_BOARD.getMsg()));

		mockMvc.perform(put("/api/v1/board/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(boardUpdateRequest)))
			.andDo(print())
			.andExpect(status().isNotFound());

	}

	// ======================삭제===============================

	@Test
	@WithMockUser
	@DisplayName("게시물 삭제 성공")
	public void successToDelete() throws Exception {
		mockMvc.perform(delete("/api/v1/board/1"))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@WithAnonymousUser
	@DisplayName("비회원 게시물 삭제 실패")
	public void anonymousToDelete() throws Exception {

		when(boardService.deleteBoard(any(), any()))
			.thenThrow(new BasicException(ErrorCode.NOT_EXIST_BOARD, ErrorCode.NOT_EXIST_BOARD.getMsg()));

		mockMvc.perform(delete("/api/v1/board/1"))
			.andDo(print())
			.andExpect(status().isUnauthorized());

	}

	@Test
	@WithMockUser
	@DisplayName("게시물 삭제시 사용자가 생성한 게시물이 아닐경우 실패")
	public void notDuplicatedToDelete() throws Exception {

		when(boardService.deleteBoard(any(), any()))
			.thenThrow(new BasicException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMsg()));

		mockMvc.perform(delete("/api/v1/board/1"))
			.andDo(print())
			.andExpect(status().isUnauthorized());

	}

	@Test
	@WithMockUser
	@DisplayName("게시물 삭제시 존재하지않는 게시판 실패")
	public void failToDelete() throws Exception {

		when(boardService.deleteBoard(any(), any()))
			.thenThrow(new BasicException(ErrorCode.NOT_EXIST_BOARD, ErrorCode.NOT_EXIST_BOARD.getMsg()));

		mockMvc.perform(delete("/api/v1/board/1"))
			.andDo(print())
			.andExpect(status().isNotFound());

	}

	//    =============피드==================

	@Test
	@DisplayName("모든 게시물 조회시 성공")
	public void successToGetBoard() throws Exception {

		mockMvc.perform(get("/api/v1/board")
				.contentType(MediaType.APPLICATION_JSON)
			).andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	@DisplayName("사용자 게시물 조회 성공")
	public void successToGetUserBoard() throws Exception {

		//        when(boardService.getUserBoard(any(),any())).thenReturn(Page.empty());
		mockMvc.perform(get("/api/v1/board/user"))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@WithAnonymousUser
	@DisplayName("사용자 게시물 조회 실패")
	public void failToGetUserBoard() throws Exception {

		mockMvc.perform(get("/api/v1/board/user"))
			.andDo(print())
			.andExpect(status().isUnauthorized());
	}

}
