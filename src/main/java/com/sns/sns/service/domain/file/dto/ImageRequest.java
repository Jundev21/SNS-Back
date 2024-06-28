package com.sns.sns.service.domain.file.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;

public record ImageRequest(
	@NotNull(message = "이미지를 업로드해주세요")
	MultipartFile userImageProfile
) {
}
