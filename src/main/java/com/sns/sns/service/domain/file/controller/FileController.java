package com.sns.sns.service.domain.file.controller;

import java.io.File;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sns.sns.service.common.basicResponse.DataResponse;
import com.sns.sns.service.domain.file.dto.ImageRequest;
import com.sns.sns.service.domain.file.dto.ImageResponse;
import com.sns.sns.service.domain.file.service.ImageService;
import com.sns.sns.service.domain.member.model.entity.Member;

import lombok.RequiredArgsConstructor;

// @RestController
// @RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class FileController {
	private final ImageService imageService;
	// @PostMapping("/image/profile")
	public DataResponse<ImageResponse> uploadUserProfile(
		@RequestParam("image") MultipartFile file,
		@AuthenticationPrincipal Member member
	){
		return DataResponse.successBodyResponse(HttpStatus.OK, imageService.uploadUserProfile(file, member));
	}

	// @GetMapping("/image/profile")
	public DataResponse<ImageResponse> getUserProfile(
		@AuthenticationPrincipal Member member
	){
		return DataResponse.successBodyResponse(HttpStatus.OK, imageService.getUserProfile(member));
	}
}
