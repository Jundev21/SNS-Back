package com.sns.sns.service.domain.board.dto.response;

import com.sns.sns.service.domain.board.model.BoardEntity;
import com.sns.sns.service.domain.member.dto.response.BasicUserInfoResponse;
import com.sns.sns.service.domain.member.model.entity.Member;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Builder
public record BoardUpdateResponse(
        Long id,
        String title,
        String content,
        BasicUserInfoResponse member,
        LocalDateTime createdTime,
        LocalDateTime updateTime

) {


    public static BoardUpdateResponse boardUpdateResponse(BoardEntity board, Member member){

        return BoardUpdateResponse.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContents())
                .member(BasicUserInfoResponse.basicUserInfoResponse(member))
                .createdTime(board.getCreatedTime())
                .build();
    }
}
