package com.sns.sns.service.domain.board.dto.response;

import java.time.LocalDateTime;

import com.sns.sns.service.domain.board.model.BoardEntity;
import com.sns.sns.service.domain.member.dto.response.BasicUserInfoResponse;
import com.sns.sns.service.domain.member.model.entity.Member;

import lombok.Builder;

@Builder
public record BoardResponse(
	Long id,
	String title,
	String contents,
	BasicUserInfoResponse member,
	LocalDateTime createdTime
) {
	public static BoardResponse boardResponse(BoardEntity board, Member member) {
		return BoardResponse.builder()
			.id(board.getId())
			.title(board.getTitle())
			.contents(board.getContents())
			.member(BasicUserInfoResponse.basicUserInfoResponse(member))
			.createdTime(board.getCreatedTime())
			.build();
	}
}
