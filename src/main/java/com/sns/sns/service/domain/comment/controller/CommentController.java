package com.sns.sns.service.domain.comment.controller;


import com.sns.sns.service.common.response.Response;
import com.sns.sns.service.domain.comment.dto.request.CommentPostRequest;
import com.sns.sns.service.domain.comment.dto.request.CommentUpdateRequest;
import com.sns.sns.service.domain.comment.dto.response.CommentGetResponse;
import com.sns.sns.service.domain.comment.dto.response.CommentPostResponse;
import com.sns.sns.service.domain.comment.dto.response.CommentUpdateResponse;
import com.sns.sns.service.domain.comment.service.CommentService;
import com.sns.sns.service.domain.member.model.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/board")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{boardId}/comment")
    public Response<CommentPostResponse> createComment(
            @PathVariable Long boardId,
            @AuthenticationPrincipal Member member,
            @RequestBody CommentPostRequest commentPostRequest
    ) {
//        commentService.createComment(boardId, member,commentPostRequest);
        return Response.success(commentService.createComment(boardId, member,commentPostRequest));
    }
    @GetMapping("/{boardId}/comment")
    public Response<List<CommentGetResponse>> getComment(
            @PathVariable Long boardId,
            @AuthenticationPrincipal Member member
    ){
        return Response.success(commentService.getComment(boardId, member));
    }
    @PutMapping("/{boardId}/comment/{commentId}")
    public Response<CommentUpdateResponse> updateComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal Member member,
            @RequestBody CommentUpdateRequest commentUpdateRequest

    ){
        return Response.success(commentService.updateComment(boardId, commentId, member,commentUpdateRequest));
    }

    @DeleteMapping("/{boardId}/comment/{commentId}")
    public Response<String> deleteComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal Member member
    ){
        commentService.deleteComment(boardId, commentId, member);
        return Response.success("DELETED");
    }

}
