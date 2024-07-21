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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user/board")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@Operation(summary = "댓글 작성 API")
	@ApiResponses(value = {
		@ApiResponse(
			description = "댓글 작성 성공",
			responseCode = "200",
			useReturnTypeSchema = true
		),
		@ApiResponse(
			description = "사용자 또는 게시판이 없는경우",
			responseCode = "404",
			content = {@Content(schema = @Schema(implementation = DataResponse.class))}
		)
	})
	@PostMapping("/{boardId}/comment")
	public DataResponse<CommentPostResponse> createComment(
		@PathVariable("boardId")  Long boardId,
		@AuthenticationPrincipal Member member,
		@RequestBody @Valid CommentPostRequest commentPostRequest
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK,
			commentService.createComment(boardId, member, commentPostRequest));
	}

	@Operation(summary = "댓글 조회 API")
	@ApiResponses(value = {
		@ApiResponse(
			description = "댓글 조회 성공",
			responseCode = "200",
			useReturnTypeSchema = true
		)
	})
	@GetMapping("/{boardId}/comment")
	public DataResponse<List<CommentGetResponse>> getComment(
		@PathVariable("boardId")  Long boardId
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK, commentService.getComment(boardId));
	}

	@Operation(summary = "댓글 수정 API")
	@ApiResponses(value = {
		@ApiResponse(
			description = "댓글 수정 성공",
			responseCode = "200",
			useReturnTypeSchema = true
		),
		@ApiResponse(
			description = "댓글이 없는경우",
			responseCode = "404",
			content = {@Content(schema = @Schema(implementation = DataResponse.class))}
		)
	})
	@PutMapping("/{boardId}/comment/{commentId}")
	public DataResponse<CommentUpdateResponse> updateComment(
		@PathVariable("boardId")  Long boardId,
		@PathVariable("commentId")  Long commentId,
		@AuthenticationPrincipal Member member,
		@RequestBody @Valid CommentUpdateRequest commentUpdateRequest

	) {
		return DataResponse.successBodyResponse(HttpStatus.OK,
			commentService.updateComment(boardId, commentId, member, commentUpdateRequest));
	}

	@Operation(summary = "댓글 삭제 API")
	@ApiResponses(value = {
		@ApiResponse(
			description = "댓글 삭제 성공",
			responseCode = "200",
			useReturnTypeSchema = true
		),
		@ApiResponse(
			description = "댓글이 없는경우",
			responseCode = "404",
			content = {@Content(schema = @Schema(implementation = DataResponse.class))}
		)
	})
	@DeleteMapping("/{boardId}/comment/{commentId}")
	public DataResponse<String> deleteComment(
		@PathVariable("commentId") Long commentId,
		@AuthenticationPrincipal Member member
	) {
		commentService.deleteComment(commentId, member);
		return DataResponse.successBodyResponse(HttpStatus.OK, "DELETED");
	}

}
