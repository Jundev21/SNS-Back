package com.sns.sns.service.domain.file.dto;

import lombok.Builder;

@Builder
public record ImageResponse(

	String imageUrl,
	String message

) {

}
