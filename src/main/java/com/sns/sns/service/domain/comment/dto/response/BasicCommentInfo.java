package com.sns.sns.service.domain.comment.dto.response;

import com.sns.sns.service.domain.comment.model.CommentEntity;
import com.sns.sns.service.domain.member.dto.response.BasicUserInfoResponse;
import lombok.Builder;

import java.time.LocalDateTime;


@Builder
public record BasicCommentInfo(
        Long commentId,
        String content,
        LocalDateTime updateDate
) {

    public static BasicCommentInfo basicCommentInfo(CommentEntity comment) {
        return BasicCommentInfo.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .updateDate(comment.getUpdatedTime()
                )
                .build();
    }
}
