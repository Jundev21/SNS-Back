package com.sns.sns.service.domain.comment.dto.response;

import com.sns.sns.service.domain.comment.model.CommentEntity;
import com.sns.sns.service.domain.member.dto.response.BasicUserInfoResponse;
import com.sns.sns.service.domain.member.model.entity.Member;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CommentGetResponse(
        BasicUserInfoResponse userInfo,
        BasicCommentInfo commentInfo
) {

    public static CommentGetResponse commentGetResponse(CommentEntity comment) {
        return CommentGetResponse.builder()
                .userInfo(BasicUserInfoResponse.basicUserInfoResponse(comment.getMember()))
                .commentInfo(BasicCommentInfo.basicCommentInfo(comment))
                .build();
    }
}
