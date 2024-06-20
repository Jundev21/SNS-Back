package com.sns.sns.service.domain.favorite.controller;


import com.sns.sns.service.common.response.Response;
import com.sns.sns.service.domain.favorite.dto.response.AddFavoriteResponse;
import com.sns.sns.service.domain.favorite.dto.response.DeleteFavoriteResponse;
import com.sns.sns.service.domain.favorite.dto.response.GetFavoriteResponse;
import com.sns.sns.service.domain.favorite.service.FavoriteService;
import com.sns.sns.service.domain.member.model.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/board/{boardId}")
    public Response<AddFavoriteResponse> addFavorite(
            @PathVariable Long boardId,
            @AuthenticationPrincipal Member member
    ) {
        return Response.success(favoriteService.addFavorite(boardId, member));
    }

    @GetMapping("/board/{boardId}")
    public Response<GetFavoriteResponse> getFavorite(
            @PathVariable Long boardId,
                        @AuthenticationPrincipal Member member

    ) {
        return Response.success(favoriteService.getFavorite(boardId,member));
    }

    @DeleteMapping("/board/{boardId}")
    public Response<DeleteFavoriteResponse> deleteFavorite(
            @PathVariable Long boardId,
             @AuthenticationPrincipal Member member
    ) {
        return Response.success(favoriteService.deleteFavorite(boardId,member));
    }
}
