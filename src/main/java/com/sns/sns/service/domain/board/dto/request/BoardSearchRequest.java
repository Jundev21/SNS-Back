package com.sns.sns.service.domain.board.dto.request;

import jakarta.validation.constraints.NotNull;

public record BoardSearchRequest(
	@NotNull(message = "검색값을 입력해주세요")
	String searchWord
) {
}
