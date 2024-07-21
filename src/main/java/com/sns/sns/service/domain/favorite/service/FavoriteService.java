package com.sns.sns.service.domain.favorite.service;

import static com.sns.sns.service.common.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sns.sns.service.common.exception.BasicException;
import com.sns.sns.service.common.exception.ErrorCode;
import com.sns.sns.service.domain.board.model.BoardEntity;
import com.sns.sns.service.domain.board.repository.BoardRepository;
import com.sns.sns.service.domain.comment.model.CommentEntity;
import com.sns.sns.service.domain.favorite.dto.response.AddFavoriteResponse;
import com.sns.sns.service.domain.favorite.dto.response.DeleteFavoriteResponse;
import com.sns.sns.service.domain.favorite.dto.response.GetFavoriteResponse;
import com.sns.sns.service.domain.favorite.model.FavoriteEntity;
import com.sns.sns.service.domain.favorite.repository.FavoriteRepository;
import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.member.repository.MemberRepository;
import com.sns.sns.service.domain.notification.dto.NotificationType;
import com.sns.sns.service.domain.notification.model.NotificationEntity;
import com.sns.sns.service.domain.notification.repository.NotificationRepository;
import com.sns.sns.service.domain.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteService {

	private final MemberRepository memberRepository;
	private final BoardRepository boardRepository;
	private final FavoriteRepository favoriteRepository;
	private final NotificationService notificationService;
	private final NotificationRepository notificationRepository;

	@Transactional
	public AddFavoriteResponse addFavorite(Long boardId, Member member) {
		Member findMember = checkExistMember(member);
		BoardEntity findBoard = findBoard(boardId);
		checkExistFavorite(findBoard, member);
		FavoriteEntity favoriteEntity = new FavoriteEntity(findBoard, findMember, true);
		favoriteRepository.save(favoriteEntity);
		Member boardOwner = findBoard.getMember();
		NotificationEntity notificationEntity = new NotificationEntity(boardOwner, NotificationType.LIKE_NOTIFICATION,
			findMember.getUserLoginId(), findBoard);
		NotificationEntity savedNotification = notificationRepository.save(notificationEntity);
		notificationService.sendToBrowser(findBoard.getMember(), findMember, savedNotification.getId());
		return AddFavoriteResponse.addFavoriteResponse(favoriteEntity);
	}

	@Transactional
	public GetFavoriteResponse getFavorite(Long boardId, Member member) {
		BoardEntity findBoard = findBoard(boardId);
		boolean isClicked = favoriteRepository.existsByBoardEntityAndMember(findBoard, member);
		return GetFavoriteResponse.getFavoriteResponse(findBoard, isClicked);
	}

	@Transactional
	public DeleteFavoriteResponse deleteFavorite(Long boardId, Member member) {
		Member findMember = checkExistMember(member);
		BoardEntity findBoard = findBoard(boardId);
		FavoriteEntity favoriteEntity = handleFavoritePermission(findBoard, findMember);
		favoriteEntity.isClickedFavorite(false);
		favoriteRepository.delete(favoriteEntity);
		return new DeleteFavoriteResponse(findBoard.getId(), findBoard.getTitle());
	}

	@Transactional(readOnly = true)
	private Member checkExistMember(Member member) {
		return memberRepository.findByUserLoginId(member.getUserLoginId())
			.orElseThrow(() -> new BasicException(ErrorCode.NOT_EXIST_MEMBER, ErrorCode.NOT_EXIST_MEMBER.getMsg()));
	}

	@Transactional(readOnly = true)
	private FavoriteEntity handleFavoritePermission(BoardEntity board, Member member) {
		return favoriteRepository
			.findFavoriteEntityByBoardEntityAndMember(board, member)
			.orElseThrow(() -> new BasicException(ErrorCode.INVALID_PERMISSION_FAVORITE,
				ErrorCode.INVALID_PERMISSION_FAVORITE.getMsg()));
	}

	@Transactional(readOnly = true)
	private void checkExistFavorite(BoardEntity board, Member member) {
		favoriteRepository
			.findFavoriteEntityByBoardEntityAndMember(board, member)
			.ifPresent(e -> {
				throw new BasicException(ErrorCode.ALREADY_EXIST_FAVORITE, ErrorCode.ALREADY_EXIST_FAVORITE.getMsg());
			});
	}

	@Transactional(readOnly = true)
	private BoardEntity findBoard(Long boardId) {
		return boardRepository.findById(boardId)
			.orElseThrow(() -> new BasicException(ErrorCode.NOT_EXIST_BOARD, ErrorCode.NOT_EXIST_BOARD.getMsg()));
	}
}
