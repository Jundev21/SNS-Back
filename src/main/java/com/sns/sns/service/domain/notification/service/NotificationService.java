package com.sns.sns.service.domain.notification.service;

import com.sns.sns.service.common.exception.BasicException;
import com.sns.sns.service.common.exception.ErrorCode;
import com.sns.sns.service.domain.comment.repository.CommentRepository;
import com.sns.sns.service.domain.favorite.repository.FavoriteRepository;
import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.member.repository.MemberRepository;
import com.sns.sns.service.domain.notification.dto.response.NotificationResponse;
import com.sns.sns.service.domain.notification.dto.response.SseResponse;
import com.sns.sns.service.domain.notification.model.NotificationEntity;
import com.sns.sns.service.domain.notification.repository.NotificationRepository;
import com.sns.sns.service.domain.notification.repository.SseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final FavoriteRepository favoriteRepository;
    private final SseRepository sseRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final String kafkaTopic = "notification";
    private final static String EVENT_NAME = "alarm";
    private final static Long DEFAULT_TIME = 30L * 60 * 1000;


//    SSE 특징 클라이언트와 서버가 커넥션을 유지한다. 클라이언트가 여러명이면 유지되는 커넥터가 많이지기때문에 서버 부하가 많아질 수 있다.
//
//    클라이언트가 네트워크 문제로 끊어지게되면 데이터 손실이 발생한다.

    public NotificationResponse makeNotification(Member member) {

        List<NotificationEntity> findNotification = notificationRepository.findAllByMemberOrderByCreatedTimeDesc(
                member);

        //알림을 받는 사용자의 커멘트 좋아요가 아니라 알림을 요청 한 사용자 => 코멘트나 라이크를 누른 사용자
        //        List<CommentEntity> commentPostResponseList = commentRepository.findAllByMember(findNotification.getMember());
        //        List<FavoriteEntity> favoriteEntityList = favoriteRepository.findAllByMember(findNotification.getMember());
        //        return NotificationResponse.notificationResponse(commentPostResponseList, favoriteEntityList, findNotification);
        return NotificationResponse.notificationResponse(null, null, findNotification, member);
    }

    public void sendToBrowser(Member receiver, Member sender, Long notificationId) {
        Member findReceiver = checkExistMember(receiver);
        Member findSender = checkExistMember(sender);
        SseResponse sseResponse = new SseResponse(findSender.getUserLoginId());

        sseRepository.get(findReceiver.getUserLoginId()).ifPresent(currentEmitter -> {
            try {
                currentEmitter.send(
                        SseEmitter.event()
                                .id(notificationId.toString())
                                .name(EVENT_NAME)
                                .data(sseResponse));
            } catch (IOException e) {
                throw new RuntimeException("can't connect");
            }
        });
    }

    // nginx http version 1.1 로 변경
    public SseEmitter sseConnector(Member member) {
        Member findMember = checkExistMember(member);
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIME);
        sseRepository.saveEmitter(findMember.getUserLoginId(), sseEmitter);
        sseEmitter.onCompletion(() ->
                log.info("SSE connection completed for user: {}", findMember.getUserLoginId()));
        sseEmitter.onTimeout(() ->
                log.info("SSE connection timed out for user: {}", findMember.getUserLoginId()));

        try {
            sseEmitter.send(
                    SseEmitter
                            .event()
                            .id("id")
                            .name(EVENT_NAME)
                            .data("success connected from server"));
            log.info("FINISHED SEND MESSAGE to " + EVENT_NAME);
        } catch (IOException e) {
            throw new RuntimeException("can't connect");
        }
        return sseEmitter;
    }

    public void sendProducer(String msg) {
        kafkaTemplate.send(kafkaTopic, msg);
    }

    //    @KafkaListener(topics = kafkaTopic, groupId = "notificationGroup")

    @Transactional(readOnly = true)
    private Member checkExistMember(Member member) {
        return memberRepository.findByUserLoginId(member.getUserLoginId())
                .orElseThrow(() -> new BasicException(ErrorCode.NOT_EXIST_MEMBER, ErrorCode.NOT_EXIST_MEMBER.getMsg()));
    }
}
