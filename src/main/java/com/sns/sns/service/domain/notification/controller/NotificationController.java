package com.sns.sns.service.domain.notification.controller;


import com.sns.sns.service.common.response.Response;
import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.notification.dto.response.NotificationResponse;
import com.sns.sns.service.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/notification")
public class NotificationController {

    private final NotificationService notificationService;

    // 알림 저장할떄
    // 어떤사용자가 어떤 타입의 알림 타입으로 보냈는지

    //알림 보낼때
    // 어떤 사용자에게 어떤 사용자가 어떤 타입의 알림을 보냈는지.

    @GetMapping
    public Response<NotificationResponse> makeNotification(
            @AuthenticationPrincipal Member member
            ){
        return Response.success(notificationService.makeNotification(member));
    }

    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    @CrossOrigin
    public SseEmitter notificationSubscribe(
           @AuthenticationPrincipal Member member
    ){
        return notificationService.sseConnector(member);
    }

    @PostMapping(value = "/kafkaProducer")
    public void notificationSubscribe(
            @RequestParam String msg
    ){
        notificationService.sendProducer(msg);
    }


}
