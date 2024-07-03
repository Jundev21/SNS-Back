
package com.sns.sns.service.domain.board.dto.response;

import java.time.LocalDateTime;

import com.sns.sns.service.domain.board.model.BoardEntity;
import com.sns.sns.service.domain.member.dto.response.BasicUserInfoResponse;

import lombok.Builder;

@Builder
public record BoardSearchResponse(
	Long id,
	String title,
	String contents,
	BasicUserInfoResponse basicUserInfoResponse,
	LocalDateTime createdTime,
	int totalFavoriteNums,
	int totalCommitNums

) {
	public static BoardSearchResponse boardResponse(BoardEntity board) {
		return BoardSearchResponse.builder()
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
