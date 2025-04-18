package com.sns.sns.service.domain.board.repository;


import com.sns.sns.service.domain.board.model.BoardEntity;
import com.sns.sns.service.domain.member.model.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    Page<BoardEntity> findAllByMember(Member member, Pageable pageable);

    Page<BoardEntity> findAllByTitleContainingOrContentsContaining(String title, String contents, Pageable pageable);
    // "select * from board where title like searchWord or content like searchWord "
}
