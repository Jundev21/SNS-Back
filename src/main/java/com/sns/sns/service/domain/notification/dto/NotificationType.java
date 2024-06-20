package com.sns.sns.service.domain.notification.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public enum NotificationType {
    COMMENT_NOTIFICATION("'댓글'을 달았습니다."),
    LIKE_NOTIFICATION("'좋아요'를 추가 하였습니다."),
    NEW_MEMBER("신규 회원을 환영합니다.");

    private final String message;
}
