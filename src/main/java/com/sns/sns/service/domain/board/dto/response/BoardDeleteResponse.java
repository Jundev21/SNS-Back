package com.sns.sns.service.domain.board.dto.response;

import com.sns.sns.service.domain.board.model.BoardEntity;
import com.sns.sns.service.domain.member.dto.response.BasicUserInfoResponse;
import com.sns.sns.service.domain.member.model.entity.Member;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BoardDeleteResponse(
        Long id,
        String title,
        String content,
        BasicUserInfoResponse member,
        LocalDateTime updateTime

) {

    public static BoardDeleteResponse boardDeleteResponse(BoardEntity board, Member member){
        return BoardDeleteResponse.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContents())
                .member(BasicUserInfoResponse.basicUserInfoResponse(member))
                .updateTime(board.getUpdatedTime())
                .build();
    }
}
