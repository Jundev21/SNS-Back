package com.sns.sns.service.domain.board.dto.request;

import jakarta.validation.constraints.NotNull;

public record BoardUpdateRequest(
	@NotNull(message = "제목을 입력해주세요.")
	String title,
	@NotNull(message = "내용을 입력해주세요.")
	String content
) {
}
