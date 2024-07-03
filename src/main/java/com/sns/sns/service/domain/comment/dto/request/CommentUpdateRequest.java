package com.sns.sns.service.domain.comment.dto.request;

import jakarta.validation.constraints.NotNull;

public record CommentUpdateRequest(
	@NotNull(message = "내용을 입력해주세요")
	String content
) {
}
