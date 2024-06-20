package com.sns.sns.service.domain.notification.dto.request;


import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.notification.dto.NotificationType;
import lombok.Builder;

@Builder
public record NotificationRequest(
        Member fromUser,
        NotificationType notificationType
) {

}
