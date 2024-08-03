package com.sns.sns.service.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import com.sns.sns.service.domain.member.model.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.test.context.support.WithMockUser;

import com.sns.sns.service.domain.board.model.BoardEntity;
import com.sns.sns.service.domain.board.repository.BoardRepository;
import com.sns.sns.service.domain.comment.model.CommentEntity;
import com.sns.sns.service.domain.comment.repository.CommentRepository;
import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.member.repository.MemberRepository;

import jakarta.persistence.EntityManager;

@DataJpaTest
public class JpaTest {

	@Autowired
	private EntityManager em;
	@Autowired
	private BoardRepository boardRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private CommentRepository commentRepository;

	@Test
	@WithMockUser
	@DisplayName("JPA 게시판 findAll 댓글없는경우")
	public void occurNPlusOne() {

		Member member = new Member("test", "test", "password", "email","img", UserStatus.JOIN);
		memberRepository.save(member);

		for (int i = 0; i < 4; i++) {
			BoardEntity boardEntity = new BoardEntity("test title", "test contents", member);
			boardRepository.save(boardEntity);
		}

		System.out.println("==========find Entity=========");
		List<BoardEntity> findAllBoard = boardRepository.findAll();
		assertEquals(findAllBoard.size(), 4);
	}

	@Test
	@WithMockUser
	@DisplayName("JPA 게시판 findAll N+1 발생 케이스")
	public void occurNPlusOneProblem() {

		Member member = new Member("test", "test", "password", "email","img", UserStatus.JOIN);
		memberRepository.save(member);

		List<BoardEntity> boardEntities = new ArrayList<>();
		List<CommentEntity> commentEntityList = new ArrayList<>();

		for (int i = 0; i < 4; i++) {
			BoardEntity boardEntity = new BoardEntity("test title", "test contents", member);
			boardEntities.add(boardEntity);

			CommentEntity commentEntity = new CommentEntity("first comment" + i, boardEntity, member);
			commentEntityList.add(commentEntity);

		}

		for (int i = 0; i < 4; i++) {
			BoardEntity boardEntity = new BoardEntity("test title", "test contents", member);
			boardEntities.add(boardEntity);

			CommentEntity commentEntity = new CommentEntity("first comment" + i, boardEntity, member);
			commentEntityList.add(commentEntity);
		}

		boardRepository.saveAll(boardEntities);
		commentRepository.saveAll(commentEntityList);

		em.flush();
		em.clear();

		System.out.println("==============================find Entity=============================");
		//        List<BoardEntity> findAllBoard = boardRepository.findAllBoard();
		List<BoardEntity> findAllBoard = boardRepository.findAll();

		//        System.out.println("==============================find comments=============================");
		//
		//        findAllBoard.stream()
		//                .forEach(board->
		//                        board.getCommentEntityList()
		//                                .forEach(comment ->
		//                                        System.out.println("Searching" + "find comment" + comment.getContent())));
		//

		//        findAllBoard.stream()
		//                .forEach(board ->
		//                        System.out.println("board content " + board.getContents()));

	}
}
