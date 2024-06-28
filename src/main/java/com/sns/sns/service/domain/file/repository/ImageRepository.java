package com.sns.sns.service.domain.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sns.sns.service.domain.file.model.ImageEntity;

public interface ImageRepository extends JpaRepository<ImageEntity,Long> {
}
