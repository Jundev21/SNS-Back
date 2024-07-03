package com.sns.sns.service.domain.favorite.repository;

import com.sns.sns.service.domain.board.model.BoardEntity;
import com.sns.sns.service.domain.favorite.model.FavoriteEntity;
import com.sns.sns.service.domain.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity,Long> {
    Optional<FavoriteEntity> findFavoriteEntityByBoardEntityAndMember(BoardEntity boardEntity, Member member);
    Optional<FavoriteEntity> findByBoardEntity(BoardEntity boardEntity);
    List<FavoriteEntity> findAllByMember(Member member);
    boolean existsByBoardEntityAndMember(BoardEntity boardEntity,Member member);
}
