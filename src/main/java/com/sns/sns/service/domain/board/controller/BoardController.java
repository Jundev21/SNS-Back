package com.sns.sns.service.domain.board.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sns.sns.service.common.basicResponse.DataResponse;
import com.sns.sns.service.domain.board.dto.request.BoardRequest;
import com.sns.sns.service.domain.board.dto.request.BoardUpdateRequest;
import com.sns.sns.service.domain.board.dto.response.BoardDeleteResponse;
import com.sns.sns.service.domain.board.dto.response.BoardDetailResponse;
import com.sns.sns.service.domain.board.dto.response.BoardGetResponse;
import com.sns.sns.service.domain.board.dto.response.BoardResponse;
import com.sns.sns.service.domain.board.dto.response.BoardUpdateResponse;
import com.sns.sns.service.domain.board.service.BoardService;
import com.sns.sns.service.domain.member.model.entity.Member;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/board")
public class BoardController {

	private final BoardService boardService;

	@PostMapping
	public DataResponse<BoardResponse> writeBoard(
		@RequestBody @Valid BoardRequest boardRequest,
		@AuthenticationPrincipal Member member
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK, boardService.writeBoard(boardRequest, member));
	}

	@PutMapping("/{boardId}")
	public DataResponse<BoardUpdateResponse> updateBoard(
		@PathVariable Long boardId,
		@RequestBody @Valid BoardUpdateRequest boardUpdateRequest,
		@AuthenticationPrincipal Member member
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK,
			boardService.updateBoard(boardId, boardUpdateRequest, member));
	}

	@DeleteMapping("/{boardId}")
	public DataResponse<BoardDeleteResponse> deleteBoard(
		@PathVariable Long boardId,
		@AuthenticationPrincipal Member member
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK, boardService.deleteBoard(boardId, member));
	}

	@GetMapping("/{boardId}")
	public DataResponse<BoardDetailResponse> deleteBoard(
		@PathVariable Long boardId
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK, boardService.getBoardDetail(boardId));
	}

	@GetMapping
	public DataResponse<Page<BoardGetResponse>> getBoard(
		Pageable pageable
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK, boardService.getBoard(pageable));
	}

	@GetMapping("/user")
	public DataResponse<Page<BoardGetResponse>> getUserBoard(
		Pageable pageable, @AuthenticationPrincipal Member member
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK, boardService.getUserBoard(pageable, member));
	}

}
