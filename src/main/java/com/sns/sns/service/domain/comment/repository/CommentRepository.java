package com.sns.sns.service.domain.comment.repository;

import com.sns.sns.service.domain.board.model.BoardEntity;
import com.sns.sns.service.domain.comment.model.CommentEntity;
import com.sns.sns.service.domain.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
    List<CommentEntity> findAllCommentEntityByBoardEntity(BoardEntity boardEntity);
    List<CommentEntity> findAllByMember(Member member);
}
