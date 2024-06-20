package com.sns.sns.service.domain.comment.dto.response;


import com.sns.sns.service.domain.comment.model.CommentEntity;
import com.sns.sns.service.domain.member.dto.response.BasicUserInfoResponse;
import lombok.Builder;

// 코멘트 응답값.
// 코멘트 작성내용.
// 작성자 아이디값, 사용자 아이디,
@Builder
public record CommentPostResponse(
        BasicUserInfoResponse userInfo,
        BasicCommentInfo commentInfo
) {

    public static CommentPostResponse commentPostResponse(CommentEntity comment){
        return CommentPostResponse.builder()
                .userInfo(BasicUserInfoResponse.basicUserInfoResponse(comment.getMember()))
                .commentInfo(BasicCommentInfo.basicCommentInfo(comment))
                .build();

    }
}
