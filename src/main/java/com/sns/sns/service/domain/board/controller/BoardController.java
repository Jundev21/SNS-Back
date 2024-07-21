package com.sns.sns.service.domain.board.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
import com.sns.sns.service.domain.board.dto.response.BoardSearchResponse;
import com.sns.sns.service.domain.board.dto.response.BoardUpdateResponse;
import com.sns.sns.service.domain.board.service.BoardService;
import com.sns.sns.service.domain.member.model.entity.Member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/board")
public class BoardController {

	private final BoardService boardService;

	@Operation(summary = "게시판 작성 API")
	@ApiResponses(value = {
		@ApiResponse(
			description = "게시판 작성 성공",
			responseCode = "200",
			useReturnTypeSchema = true
		),
		@ApiResponse(
			description = "작성자가 존재하지 않을경우",
			responseCode = "404",
			content = {@Content(schema = @Schema(implementation = DataResponse.class))}
		)
	})
	@PostMapping
	public DataResponse<BoardResponse> writeBoard(
		@RequestBody @Valid BoardRequest boardRequest,
		@AuthenticationPrincipal Member member
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK, boardService.writeBoard(boardRequest, member));
	}

	@Operation(summary = "게시판 수정 API")
	@ApiResponses(value = {
		@ApiResponse(
			description = "게시판 수정 성공",
			responseCode = "200",
			useReturnTypeSchema = true
		),
		@ApiResponse(
			description = "게시판이 존재하지 않을경우",
			responseCode = "404",
			content = {@Content(schema = @Schema(implementation = DataResponse.class))}
		)
	})
	@PutMapping("/{boardId}")
	public DataResponse<BoardUpdateResponse> updateBoard(
		@PathVariable("boardId") Long boardId,
		@RequestBody @Valid BoardUpdateRequest boardUpdateRequest,
		@AuthenticationPrincipal Member member
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK,
			boardService.updateBoard(boardId, boardUpdateRequest, member));
	}

	@Operation(summary = "게시판 삭제 API")
	@ApiResponses(value = {
		@ApiResponse(
			description = "게시판 삭제 성공",
			responseCode = "200",
			useReturnTypeSchema = true
		),
		@ApiResponse(
			description = "게시판이 존재하지 않을경우",
			responseCode = "404",
			content = {@Content(schema = @Schema(implementation = DataResponse.class))}
		)
	})
	@DeleteMapping("/{boardId}")
	public DataResponse<BoardDeleteResponse> deleteBoard(
		@PathVariable("boardId") Long boardId,
		@AuthenticationPrincipal Member member
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK, boardService.deleteBoard(boardId, member));
	}

	@Operation(summary = "게시판 상세 조회 API")
	@ApiResponses(value = {
		@ApiResponse(
			description = "게시판 상세 조회 성공",
			responseCode = "200",
			useReturnTypeSchema = true
		),
		@ApiResponse(
			description = "게시판이 존재하지 않을경우",
			responseCode = "404",
			content = {@Content(schema = @Schema(implementation = DataResponse.class))}
		)
	})
	@GetMapping("/{boardId}")
	public DataResponse<BoardDetailResponse> getDetailBoard(
		@PathVariable("boardId") Long boardId
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK, boardService.getBoardDetail(boardId));
	}

	@Operation(summary = "게시판 전체 조회 API")
	@ApiResponses(value = {
		@ApiResponse(
			description = "게시판 전체 조회 성공",
			responseCode = "200",
			useReturnTypeSchema = true
		)
	})
	@GetMapping
	public DataResponse<Page<BoardGetResponse>> getAllBoard(
		@PageableDefault(size = 12, sort = "createdTime", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK, boardService.getBoard(pageable));
	}

	@Operation(summary = "나의 게시판 조회 API")
	@ApiResponses(value = {
		@ApiResponse(
			description = "나의 게시판 조회 성공",
			responseCode = "200",
			useReturnTypeSchema = true
		),
		@ApiResponse(
			description = "사용자가 없는 경우",
			responseCode = "404",
			content = {@Content(schema = @Schema(implementation = DataResponse.class))}
		)
	})
	@GetMapping("/user")
	public DataResponse<Page<BoardGetResponse>> getUserBoard(
		@PageableDefault(size = 12, sort = "createdTime", direction = Sort.Direction.DESC) Pageable pageable,
		@AuthenticationPrincipal Member member
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK, boardService.getUserBoard(pageable, member));
	}

	@Operation(summary = "게시판 검색 조회 API")
	@ApiResponses(value = {
		@ApiResponse(
			description = "게시판 검색 조회 성공",
			responseCode = "200",
			useReturnTypeSchema = true
		)
	})
	@GetMapping("/searching/{searchWord}")
	public DataResponse<Page<BoardSearchResponse>> getSearchResult(
		@PageableDefault(size = 12, sort = "createdTime", direction = Sort.Direction.DESC) Pageable pageable,
		@PathVariable("searchWord") String searchWord
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK, boardService.getSearchWord(searchWord, pageable));
	}

}
