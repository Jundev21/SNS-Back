package com.sns.sns.service.domain.notification.service;


import com.sns.sns.service.domain.comment.repository.CommentRepository;
import com.sns.sns.service.domain.exception.BasicException;
import com.sns.sns.service.domain.exception.ErrorCode;
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
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


import java.io.IOException;
import java.util.List;

import static com.sns.sns.service.domain.exception.ErrorCode.NOT_EXIST_MEMBER;

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
    private final static Long DEFAULT_TIME = 60L * 1000 * 60;

    //polling 방식 - 프론트엔드 계속해서 데이터 요청을 보내야한다.
    public NotificationResponse makeNotification(Member member) {

        //알림을 받는 사용자가아니라
        List<NotificationEntity> findNotification = notificationRepository.findAllByMemberOrderByCreatedTimeDesc(member);

        //알림을 받는 사용자의 커멘트 좋아요가 아니라 알림을 요청 한 사용자 => 코멘트나 라이크를 누른 사용자
//        List<CommentEntity> commentPostResponseList = commentRepository.findAllByMember(findNotification.getMember());
//        List<FavoriteEntity> favoriteEntityList = favoriteRepository.findAllByMember(findNotification.getMember());
//        return NotificationResponse.notificationResponse(commentPostResponseList, favoriteEntityList, findNotification);
        return NotificationResponse.notificationResponse(null, null, findNotification,member);
    }

    public void sendToBrowser(Member receiver, Member sender, Long notificationId) {
        Member findMember = memberRepository.findById(receiver.getId())
                .orElseThrow(() -> new BasicException(NOT_EXIST_MEMBER, ErrorCode.NOT_EXIST_BOARD.getMessage()));

        SseResponse sseResponse = new SseResponse(sender.getUsername());
        sseRepository.get(findMember.getId()).ifPresent(currentEmitter -> {
            try {
                currentEmitter.send(
                        SseEmitter.event()
                                .id(notificationId.toString())
                                .name(EVENT_NAME)
                                .data(sseResponse));
            } catch (IOException e) {
                sseRepository.delete(findMember.getId());
                throw new RuntimeException("can't connect");
            }
        });
    }

    public SseEmitter sseConnector(Member member) {

        Member findMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new BasicException(NOT_EXIST_MEMBER, ErrorCode.NOT_EXIST_BOARD.getMessage()));

        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIME);
        sseRepository.saveEmitter(findMember.getId(), sseEmitter);

        sseEmitter.onCompletion(() -> sseRepository.delete(findMember.getId()));
        sseEmitter.onTimeout(() -> sseRepository.delete(findMember.getId()));

        try {
             sseEmitter.send(
                    SseEmitter
                            .event()
                            .id("id")
                            .name(EVENT_NAME)
                            .data("connected"));


        } catch (IOException e) {
            throw new RuntimeException("can't connect");
        }
        return sseEmitter;
    }

    public void sendProducer(String msg){
        kafkaTemplate.send(kafkaTopic, msg);
    }

//    @KafkaListener(topics = kafkaTopic, groupId = "notificationGroup")
    public void notificationConsumer(String message){
        System.out.printf("Subscribed : " + message);
    }
}
