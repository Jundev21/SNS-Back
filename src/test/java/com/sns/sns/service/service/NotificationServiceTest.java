package com.sns.sns.service.service;


import com.sns.sns.service.domain.board.dto.request.BoardRequest;
import com.sns.sns.service.domain.board.model.BoardEntity;
import com.sns.sns.service.domain.board.repository.BoardRepository;
import com.sns.sns.service.domain.comment.model.CommentEntity;
import com.sns.sns.service.domain.comment.repository.CommentRepository;
import com.sns.sns.service.domain.favorite.model.FavoriteEntity;
import com.sns.sns.service.domain.favorite.repository.FavoriteRepository;
import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.member.repository.MemberRepository;
import com.sns.sns.service.domain.notification.dto.NotificationType;
import com.sns.sns.service.domain.notification.dto.response.NotificationResponse;
import com.sns.sns.service.domain.notification.model.NotificationEntity;
import com.sns.sns.service.domain.notification.repository.NotificationRepository;
import com.sns.sns.service.domain.notification.service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static com.sns.sns.service.data.BoardData.createNewBoard;
import static com.sns.sns.service.data.CommentData.commentEntityData;
import static com.sns.sns.service.data.UserData.userEntity;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class NotificationServiceTest {


    @Autowired
    private NotificationService notificationService;
    @MockBean
    private FavoriteRepository favoriteRepository;
    @MockBean
    private BoardRepository boardRepository;
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private NotificationRepository notificationRepository;
    @MockBean
    private CommentRepository commentRepository;

    @Test
    @DisplayName("알림을 전달 받을 수 있다.")
    public void getNotificationSuccess(){

        Member member = userEntity("test user", "password");
        Member secondMember = userEntity("second user", "password");

        BoardRequest boardRequest = new BoardRequest("new board title", " new board contents");
        BoardEntity boardEntity = createNewBoard(boardRequest, member);

        CommentEntity commentEntity = commentEntityData("first comment", boardEntity, secondMember);
        CommentEntity commentEntityTwo = commentEntityData("second comment", boardEntity, secondMember);

//        FavoriteEntity favorite = new FavoriteEntity(boardEntity,secondMember);
//        FavoriteEntity favoriteTwo = new FavoriteEntity(boardEntity,secondMember);

        List<NotificationEntity> notificationEntityList =
                List.of(
//                        new NotificationEntity(member, NotificationType.LIKE_NOTIFICATION),
//                        new NotificationEntity(member, NotificationType.LIKE_NOTIFICATION),
//                        new NotificationEntity(member, NotificationType.COMMENT_NOTIFICATION)
                );

//        given( notificationRepository.findAllByMember(any())).willReturn(notificationEntityList);

//        NotificationResponse result = notificationService.makeNotification(member);
        NotificationResponse result2 = notificationService.makeNotification(secondMember);


//        System.out.println(result);
        System.out.println(result2);

    }


}
