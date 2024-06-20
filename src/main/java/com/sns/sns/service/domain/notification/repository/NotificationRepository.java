package com.sns.sns.service.domain.notification.repository;

import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.notification.model.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    List<NotificationEntity> findAllByMemberOrderByCreatedTimeDesc(Member member);
}
