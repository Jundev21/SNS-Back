package com.sns.sns.service.domain.favorite.model;


import com.sns.sns.service.common.BaseTimeEntity;
import com.sns.sns.service.domain.board.model.BoardEntity;
import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.notification.model.NotificationEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class FavoriteEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isClicked;
    @ManyToOne
    private BoardEntity boardEntity;
    @ManyToOne
    private Member member;
    @ManyToOne
    private NotificationEntity notificationEntity;

    public FavoriteEntity(
            BoardEntity board,
            Member member,
            boolean isClicked
    ){
        this.boardEntity = board;
        this.member = member;
        this.isClicked = isClicked;
    }

    public void isClickedFavorite(boolean isClicked){
        this.isClicked = isClicked;
    }

}
