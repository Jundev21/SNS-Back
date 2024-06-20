package com.sns.sns.service.domain.board.dto.response;

import com.sns.sns.service.domain.board.model.BoardEntity;
import com.sns.sns.service.domain.member.dto.response.BasicUserInfoResponse;
import com.sns.sns.service.domain.member.model.entity.Member;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;


@Builder
public record BoardGetResponse(
        Long id,
        String title,
        String contents,
        BasicUserInfoResponse basicUserInfoResponse,
        LocalDateTime createdTime,
        int totalFavoriteNums,
        int totalCommitNums

) {

    public static BoardGetResponse boardGetResponse(BoardEntity board){
        return BoardGetResponse.builder()
                .id(board.getId())
                .title(board.getTitle())
                .contents(board.getContents())
                .totalFavoriteNums(board.getFavoriteEntityList().size())
                .totalCommitNums(board.getCommentEntityList().size())
                .basicUserInfoResponse(BasicUserInfoResponse.basicUserInfoResponse(board.getMember()))
                .createdTime(board.getCreatedTime())
                .build();
    }

}
