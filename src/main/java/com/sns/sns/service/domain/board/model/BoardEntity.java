package com.sns.sns.service.domain.board.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Formula;

import com.sns.sns.service.common.BaseTimeEntity;
import com.sns.sns.service.domain.board.dto.request.BoardUpdateRequest;
import com.sns.sns.service.domain.comment.model.CommentEntity;
import com.sns.sns.service.domain.favorite.model.FavoriteEntity;
import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.notification.model.NotificationEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class BoardEntity extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	@Column(columnDefinition = "TEXT")
	private String contents;
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;
	@OneToMany(mappedBy = "boardEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<NotificationEntity> notificationEntityList;
	@OneToMany(mappedBy = "boardEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<CommentEntity> commentEntityList;
	@OneToMany(mappedBy = "boardEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<FavoriteEntity> favoriteEntityList;
	@Formula("(select count(*) from favorite_entity where favorite_entity.board_entity_id=id)")
	private long countFavorite;
	@Formula("(select count(*) from comment_entity where comment_entity.board_entity_id=id)")
	private long countComments;

	//좋아요는 순서 상관없이 추가 삭제가 유용함으로 이부분 map 으로 리팩토링 해보기
	public BoardEntity(
		String title,
		String contents,
		Member member
	) {
		this.title = title;
		this.contents = contents;
		this.member = member;
	}

	public BoardEntity updateBoard(BoardUpdateRequest boardUpdateRequest) {
		this.title = boardUpdateRequest.title();
		this.contents = boardUpdateRequest.content();
		return this;
	}
}
