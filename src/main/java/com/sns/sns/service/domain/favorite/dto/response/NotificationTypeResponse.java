package com.sns.sns.service.domain.favorite.dto.response;

import com.sns.sns.service.domain.board.dto.response.BoardGetResponse;
import com.sns.sns.service.domain.notification.dto.NotificationType;
import com.sns.sns.service.domain.notification.model.NotificationEntity;
import lombok.Builder;

import java.time.LocalDateTime;


@Builder
public record NotificationTypeResponse(
        String message,
        String sender,
        LocalDateTime notificationTime,
        BoardGetResponse targetBoard

) {

    public static NotificationTypeResponse notificationTypeBuilder(NotificationEntity notification) {
        return NotificationTypeResponse.builder()
                .message(notification.getNotificationType().getMessage())
                .notificationTime(notification.getCreatedTime())
                .sender(notification.getSender())
                .targetBoard(BoardGetResponse.boardGetResponse(notification.getBoardEntity()))
                .build();

    }

}

