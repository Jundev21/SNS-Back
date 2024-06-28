package com.sns.sns.service.domain.file.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sns.sns.service.domain.file.dto.ImageResponse;
import com.sns.sns.service.domain.file.repository.ImageRepository;
import com.sns.sns.service.domain.member.model.entity.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {

	private final ImageRepository imageRepository;


	public ImageResponse uploadUserProfile(MultipartFile imageRequest, Member member) {

		return null;

	}


	public ImageResponse getUserProfile(Member member) {
		return null;
	}
}
