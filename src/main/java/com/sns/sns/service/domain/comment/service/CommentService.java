package com.sns.sns.service.domain.comment.service;

import static com.sns.sns.service.common.exception.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sns.sns.service.common.exception.BasicException;
import com.sns.sns.service.common.exception.ErrorCode;
import com.sns.sns.service.domain.board.model.BoardEntity;
import com.sns.sns.service.domain.board.repository.BoardRepository;
import com.sns.sns.service.domain.comment.dto.request.CommentPostRequest;
import com.sns.sns.service.domain.comment.dto.request.CommentUpdateRequest;
import com.sns.sns.service.domain.comment.dto.response.CommentGetResponse;
import com.sns.sns.service.domain.comment.dto.response.CommentPostResponse;
import com.sns.sns.service.domain.comment.dto.response.CommentUpdateResponse;
import com.sns.sns.service.domain.comment.model.CommentEntity;
import com.sns.sns.service.domain.comment.repository.CommentRepository;
import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.member.repository.MemberRepository;
import com.sns.sns.service.domain.notification.dto.NotificationType;
import com.sns.sns.service.domain.notification.model.NotificationEntity;
import com.sns.sns.service.domain.notification.repository.NotificationRepository;
import com.sns.sns.service.domain.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final MemberRepository memberRepository;
	private final BoardRepository boardRepository;
	private final NotificationRepository notificationRepository;
	private final NotificationService notificationService;

	@Transactional
	public CommentPostResponse createComment(Long boardId, Member member, CommentPostRequest commentPostRequest) {
		Member findMember = checkExistMember(member);
		BoardEntity findBoard = findBoard(boardId);
		CommentEntity newComment = new CommentEntity(commentPostRequest.content(), findBoard, findMember);
		CommentEntity savedComment = commentRepository.save(newComment);
		NotificationEntity savedNotification = saveNotification(findBoard, findMember.getUserLoginId());
		notificationService.sendToBrowser(findBoard.getMember(), findMember, savedNotification.getId());
		return CommentPostResponse.commentPostResponse(savedComment);
	}

	@Transactional(readOnly = true)
	public List<CommentGetResponse> getComment(Long boardId) {
		BoardEntity findBoard = findBoard(boardId);
		List<CommentEntity> commentEntityList = commentRepository.findAllCommentEntityByBoardEntity(findBoard);
		return commentEntityList.stream()
			.map(CommentGetResponse::commentGetResponse)
			.toList();
	}

	@Transactional
	public CommentUpdateResponse updateComment(Long boardId, Long commentId, Member member,
		CommentUpdateRequest commentUpdateRequest) {
		Member findMember = checkExistMember(member);
		findBoard(boardId);
		CommentEntity findComment = findComment(commentId);
		findComment.updateComment(commentUpdateRequest.content());
		commentEditPermission(findMember, findComment);
		return CommentUpdateResponse.commentUpdateResponse(findComment);
	}

	@Transactional
	public void deleteComment(Long commentId, Member member) {
		Member findMember = checkExistMember(member);
		CommentEntity findComment = findComment(commentId);
		commentEditPermission(findMember, findComment);
		commentRepository.delete(findComment);
	}

	@Transactional(readOnly = true)
	public void commentEditPermission(Member member, CommentEntity comment) {
		if (!member.getUserLoginId().equals(comment.getMember().getUserLoginId())) {
			throw new BasicException(INVALID_PERMISSION_COMMENT, INVALID_PERMISSION_COMMENT.getMsg());
		}
	}

	@Transactional(readOnly = true)
	public NotificationEntity saveNotification(BoardEntity boardEntity, String sender) {
		Member boardOwner = boardEntity.getMember();
		NotificationEntity notificationEntity = new NotificationEntity(boardOwner,
			NotificationType.COMMENT_NOTIFICATION, sender, boardEntity);
		return notificationRepository.save(notificationEntity);
	}

	@Transactional(readOnly = true)
	public void deleteNotification(NotificationEntity notificationEntity) {
		NotificationEntity findNotification = notificationRepository.findById(notificationEntity.getId())
			.orElseThrow(() -> new BasicException(NOT_EXIST_NOTIFICATION, NOT_EXIST_NOTIFICATION.getMsg()));
		notificationRepository.delete(notificationEntity);
	}

	@Transactional(readOnly = true)
	private Member checkExistMember(Member member) {
		return memberRepository.findByUserName(member.getUserLoginId())
			.orElseThrow(() -> new BasicException(ErrorCode.NOT_EXIST_MEMBER, ErrorCode.NOT_EXIST_MEMBER.getMsg()));
	}

	@Transactional(readOnly = true)
	private CommentEntity findComment(Long commentId) {
		return commentRepository.findById(commentId)
			.orElseThrow(() -> new BasicException(NOT_EXIST_COMMENT, NOT_EXIST_COMMENT.getMsg()));
	}

	@Transactional(readOnly = true)
	private BoardEntity findBoard(Long boardId) {
		return boardRepository.findById(boardId)
			.orElseThrow(() -> new BasicException(NOT_EXIST_BOARD, NOT_EXIST_BOARD.getMsg()));
	}
}
