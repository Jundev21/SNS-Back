package com.sns.sns.service.domain.favorite.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sns.sns.service.common.basicResponse.DataResponse;
import com.sns.sns.service.domain.favorite.dto.response.AddFavoriteResponse;
import com.sns.sns.service.domain.favorite.dto.response.DeleteFavoriteResponse;
import com.sns.sns.service.domain.favorite.dto.response.GetFavoriteResponse;
import com.sns.sns.service.domain.favorite.service.FavoriteService;
import com.sns.sns.service.domain.member.model.entity.Member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/favorite")
@RequiredArgsConstructor
public class FavoriteController {

	private final FavoriteService favoriteService;

	@Operation(summary = "좋아요 추가 API")
	@ApiResponses(value = {
		@ApiResponse(
			description = "좋아요 추가 성공",
			responseCode = "200",
			useReturnTypeSchema = true
		),
		@ApiResponse(
			description = "사용자 또는 게시판이 없는경우",
			responseCode = "404",
			content = {@Content(schema = @Schema(implementation = DataResponse.class))}
		)
	})
	@PostMapping("/board/{boardId}")
	public DataResponse<AddFavoriteResponse> addFavorite(
		@PathVariable("boardId") Long boardId,
		@AuthenticationPrincipal Member member
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK, favoriteService.addFavorite(boardId, member));
	}

	@Operation(summary = "좋아요 조회 API")
	@ApiResponses(value = {
		@ApiResponse(
			description = "좋아요 조회 성공",
			responseCode = "200",
			useReturnTypeSchema = true
		),
		@ApiResponse(
			description = "사용자 또는 게시판이 없는경우",
			responseCode = "404",
			content = {@Content(schema = @Schema(implementation = DataResponse.class))}
		)
	})
	@GetMapping("/board/{boardId}")
	public DataResponse<GetFavoriteResponse> getFavorite(
		@PathVariable("boardId") Long boardId,
		@AuthenticationPrincipal Member member

	) {
		return DataResponse.successBodyResponse(HttpStatus.OK, favoriteService.getFavorite(boardId, member));
	}

	@Operation(summary = "좋아요 삭제 API")
	@ApiResponses(value = {
		@ApiResponse(
			description = "좋아요 추가 성공",
			responseCode = "200",
			useReturnTypeSchema = true
		),
		@ApiResponse(
			description = "사용자 또는 게시판이 없는경우",
			responseCode = "404",
			content = {@Content(schema = @Schema(implementation = DataResponse.class))}
		)
	})
	@DeleteMapping("/board/{boardId}")
	public DataResponse<DeleteFavoriteResponse> deleteFavorite(
		@PathVariable("boardId") Long boardId,
		@AuthenticationPrincipal Member member
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK, favoriteService.deleteFavorite(boardId, member));
	}
}
