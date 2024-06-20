package com.sns.sns.service.domain.comment.dto.response;

import com.sns.sns.service.domain.comment.model.CommentEntity;
import com.sns.sns.service.domain.member.dto.response.BasicUserInfoResponse;
import lombok.Builder;

@Builder
public record CommentUpdateResponse(
        BasicUserInfoResponse userInfo,
        BasicCommentInfo commentInfo
) {

    public static CommentUpdateResponse commentUpdateResponse(CommentEntity comment){
        return CommentUpdateResponse.builder()
                .userInfo(BasicUserInfoResponse.basicUserInfoResponse(comment.getMember()))
                .commentInfo(BasicCommentInfo.basicCommentInfo(comment))
                .build();

    }
}