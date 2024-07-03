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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/favorite")
@RequiredArgsConstructor
public class FavoriteController {

	private final FavoriteService favoriteService;

	@PostMapping("/board/{boardId}")
	public DataResponse<AddFavoriteResponse> addFavorite(
		@PathVariable Long boardId,
		@AuthenticationPrincipal Member member
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK, favoriteService.addFavorite(boardId, member));
	}

	@GetMapping("/board/{boardId}")
	public DataResponse<GetFavoriteResponse> getFavorite(
		@PathVariable Long boardId,
		@AuthenticationPrincipal Member member

	) {
		return DataResponse.successBodyResponse(HttpStatus.OK, favoriteService.getFavorite(boardId, member));
	}

	@DeleteMapping("/board/{boardId}")
	public DataResponse<DeleteFavoriteResponse> deleteFavorite(
		@PathVariable Long boardId,
		@AuthenticationPrincipal Member member
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK, favoriteService.deleteFavorite(boardId, member));
	}
}
