package com.sns.sns.service.domain.board.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sns.sns.service.common.exception.BasicException;
import com.sns.sns.service.common.exception.ErrorCode;
import com.sns.sns.service.domain.board.dto.request.BoardRequest;
import com.sns.sns.service.domain.board.dto.request.BoardUpdateRequest;
import com.sns.sns.service.domain.board.dto.response.BoardDeleteResponse;
import com.sns.sns.service.domain.board.dto.response.BoardDetailResponse;
import com.sns.sns.service.domain.board.dto.response.BoardGetResponse;
import com.sns.sns.service.domain.board.dto.response.BoardResponse;
import com.sns.sns.service.domain.board.dto.response.BoardSearchResponse;
import com.sns.sns.service.domain.board.dto.response.BoardUpdateResponse;
import com.sns.sns.service.domain.board.model.BoardEntity;
import com.sns.sns.service.domain.board.repository.BoardRepository;
import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final MemberRepository memberRepository;
	private final BoardRepository boardRepository;

	@Transactional
	public BoardResponse writeBoard(BoardRequest boardRequest, Member member) {
		checkExistMember(member);
		BoardEntity newBoard = new BoardEntity(boardRequest.title(), boardRequest.contents(), member);
		BoardEntity savedBoard = boardRepository.save(newBoard);
		return BoardResponse.boardResponse(savedBoard, member);
	}

	@Transactional
	public BoardUpdateResponse updateBoard(Long boardId, BoardUpdateRequest boardUpdateRequest, Member member) {
		Member findMember = checkExistMember(member);
		BoardEntity findBoard = getBoardEntity(boardId);
		checkUpdatePermission(member, findBoard);
		BoardEntity updatedBoard = findBoard.updateBoard(boardUpdateRequest);
		return BoardUpdateResponse.boardUpdateResponse(updatedBoard, findMember);
	}

	@Transactional
	public BoardDeleteResponse deleteBoard(Long boardId, Member member) {
		Member findMember = checkExistMember(member);
		BoardEntity findBoard = getBoardEntity(boardId);
		checkUpdatePermission(member, findBoard);
		boardRepository.delete(findBoard);
		return BoardDeleteResponse.boardDeleteResponse(findBoard, findMember);
	}

	@Transactional(readOnly = true)
	public Page<BoardSearchResponse> getSearchWord(String searchWord, Pageable pageable) {
		Page<BoardEntity> boardEntities = boardRepository.findAllByTitleContainingOrContentsContaining(searchWord,
			searchWord, pageable);
		return boardEntities.map(BoardSearchResponse::boardResponse);
	}

	@Transactional(readOnly = true)
	public Page<BoardGetResponse> getBoard(Pageable pageable) {
		Page<BoardEntity> board = boardRepository.findAll(pageable);
		return board.map(BoardGetResponse::boardGetResponse);
	}

	@Transactional(readOnly = true)
	public Page<BoardGetResponse> getUserBoard(Pageable pageable, Member member) {
		Member findMember = checkExistMember(member);
		Page<BoardEntity> boardEntities = boardRepository.findAllByMember(findMember, pageable);
		return boardEntities.map(BoardGetResponse::boardGetResponse);
	}

	@Transactional(readOnly = true)
	public BoardDetailResponse getBoardDetail(Long boardId) {
		BoardEntity findBoard = getBoardEntity(boardId);
		return BoardDetailResponse.boardDetailResponse(findBoard);
	}

	@Transactional(readOnly = true)
	private Member checkExistMember(Member member) {
		return memberRepository.findByUserName(member.getUserLoginId())
			.orElseThrow(() -> new BasicException(ErrorCode.NOT_EXIST_MEMBER, ErrorCode.NOT_EXIST_MEMBER.getMsg()));
	}

	@Transactional(readOnly = true)
	private BoardEntity getBoardEntity(Long boardId) {
		return boardRepository.findById(boardId)
			.orElseThrow(() -> new BasicException(ErrorCode.NOT_EXIST_BOARD, ErrorCode.NOT_EXIST_BOARD.getMsg()));
	}

	@Transactional(readOnly = true)
	private void checkUpdatePermission(Member member, BoardEntity board) {
		if (!member.getUserLoginId().equals(board.getMember().getUserLoginId())) {
			throw new BasicException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMsg());
		}
	}

}
