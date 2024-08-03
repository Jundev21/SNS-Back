package com.sns.sns.service.data;

import com.sns.sns.service.domain.board.model.BoardEntity;
import com.sns.sns.service.domain.comment.dto.request.CommentPostRequest;
import com.sns.sns.service.domain.comment.dto.request.CommentUpdateRequest;
import com.sns.sns.service.domain.comment.dto.response.CommentGetResponse;
import com.sns.sns.service.domain.comment.dto.response.CommentPostResponse;
import com.sns.sns.service.domain.comment.model.CommentEntity;
import com.sns.sns.service.domain.member.model.UserStatus;
import com.sns.sns.service.domain.member.model.entity.Member;

public class CommentData {

	public static CommentPostResponse commentPostResponseData() {

		Member member = new Member("test", "test", "password", "email","img", UserStatus.JOIN);
		BoardEntity boardEntity = new BoardEntity("board title", "board content", member);
		CommentEntity comment = new CommentEntity("comment content", boardEntity, member);
		return CommentPostResponse.commentPostResponse(comment);
	}

	public static CommentPostRequest commentPostRequestData() {

		return new CommentPostRequest("comment request data");
	}

	public static CommentUpdateRequest commentUpdateRequest() {

		return new CommentUpdateRequest("comment update data");
	}

	public static CommentGetResponse commentGetResponseData() {
		Member member = new Member("test", "test", "password", "email","img", UserStatus.JOIN);
		BoardEntity boardEntity = new BoardEntity("board title", "board content", member);
		CommentEntity comment = new CommentEntity("comment content", boardEntity, member);
		return CommentGetResponse.commentGetResponse(comment);
	}

	public static CommentEntity commentEntityData(String content, BoardEntity boardEntity, Member member) {
		return new CommentEntity("comment content", boardEntity, member);
	}
}
