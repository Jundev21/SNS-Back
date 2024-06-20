package com.sns.sns.service.domain.comment.model;


import com.sns.sns.service.common.BaseTimeEntity;
import com.sns.sns.service.domain.board.model.BoardEntity;
import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.notification.model.NotificationEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.w3c.dom.Text;

import javax.management.Notification;

@Entity
@Getter
@NoArgsConstructor
public class CommentEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String content;
    @ManyToOne
    private BoardEntity boardEntity;
    @ManyToOne
    private Member member;
    @ManyToOne
    private NotificationEntity notificationEntity;


    public CommentEntity(String content, BoardEntity boardEntity, Member member){
        this.content = content;
        this.boardEntity = boardEntity;
        this.member = member;
    }

    public void updateComment(String content){
        this.content = content;
    }
}
