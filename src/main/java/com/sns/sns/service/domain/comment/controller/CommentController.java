package com.sns.sns.service.domain.comment.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sns.sns.service.common.basicResponse.DataResponse;
import com.sns.sns.service.domain.comment.dto.request.CommentPostRequest;
import com.sns.sns.service.domain.comment.dto.request.CommentUpdateRequest;
import com.sns.sns.service.domain.comment.dto.response.CommentGetResponse;
import com.sns.sns.service.domain.comment.dto.response.CommentPostResponse;
import com.sns.sns.service.domain.comment.dto.response.CommentUpdateResponse;
import com.sns.sns.service.domain.comment.service.CommentService;
import com.sns.sns.service.domain.member.model.entity.Member;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user/board")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@PostMapping("/{boardId}/comment")
	public DataResponse<CommentPostResponse> createComment(
		@PathVariable Long boardId,
		@AuthenticationPrincipal Member member,
		@RequestBody @Valid CommentPostRequest commentPostRequest
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK,
			commentService.createComment(boardId, member, commentPostRequest));
	}

	@GetMapping("/{boardId}/comment")
	public DataResponse<List<CommentGetResponse>> getComment(
		@PathVariable Long boardId
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK, commentService.getComment(boardId));
	}

	@PutMapping("/{boardId}/comment/{commentId}")
	public DataResponse<CommentUpdateResponse> updateComment(
		@PathVariable Long boardId,
		@PathVariable Long commentId,
		@AuthenticationPrincipal Member member,
		@RequestBody @Valid CommentUpdateRequest commentUpdateRequest

	) {
		return DataResponse.successBodyResponse(HttpStatus.OK,
			commentService.updateComment(boardId, commentId, member, commentUpdateRequest));
	}

	@DeleteMapping("/{boardId}/comment/{commentId}")
	public DataResponse<String> deleteComment(
		@PathVariable Long commentId,
		@AuthenticationPrincipal Member member
	) {
		commentService.deleteComment(commentId, member);
		return DataResponse.successBodyResponse(HttpStatus.OK, "DELETED");
	}

}
