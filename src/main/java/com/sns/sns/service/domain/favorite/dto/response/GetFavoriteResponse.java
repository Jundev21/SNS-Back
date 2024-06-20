package com.sns.sns.service.domain.favorite.dto.response;

import com.sns.sns.service.domain.board.model.BoardEntity;
import com.sns.sns.service.domain.favorite.model.FavoriteEntity;
import com.sns.sns.service.domain.member.dto.response.BasicUserInfoResponse;
import com.sns.sns.service.domain.member.model.entity.Member;
import lombok.Builder;


@Builder
public record GetFavoriteResponse(
        int favoriteNumber,
        boolean isClicked

) {

    public  static  GetFavoriteResponse getFavoriteResponse(BoardEntity board, boolean isClicked){
        return GetFavoriteResponse.builder()
                .favoriteNumber(board.getFavoriteEntityList().size())
                .isClicked(isClicked)
                .build();
    }

}
