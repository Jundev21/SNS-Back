package com.sns.sns.service.domain.file.model;

import com.sns.sns.service.domain.member.model.entity.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ImageEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String imageName;
	private String imageUrl;
	@ManyToOne
	private Member member;

	public ImageEntity(String imageName, String imageUrl, Member member) {
		this.imageName = imageName;
		this.imageUrl = imageUrl;
		this.member = member;
	}
}
