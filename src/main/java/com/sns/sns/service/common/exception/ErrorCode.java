package com.sns.sns.service.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
	ALREADY_EXIST_MEMBER(HttpStatus.CONFLICT, "존재하는 회원이 있습니다."),
	NOT_EXIST_MEMBER(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
	INVALID_LOGIN_INFO(HttpStatus.BAD_REQUEST, "로그인 정보가 다릅니다. 다시확인해주세요."),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
	INVALID_SORT_VALUE(HttpStatus.BAD_REQUEST, "정렬값이 올바르지 않습니다. "),
	UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "사용자 권한이 없습니다."),
	MISS_MATCH_PWD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
	EMPTY_FILE(HttpStatus.BAD_REQUEST, "파일이 비어있습니다."),
	ALREADY_EXIST_FAVORITE(HttpStatus.CONFLICT, "이미 좋아요가 존재합니다."),
	FAILED_GOOGLE_AUTHENTICATION(HttpStatus.CONFLICT, "구글 드라이브 사용자 인증실패했습니다."),
	FAILED_GOOGLE_IMAGE(HttpStatus.CONFLICT, "파일 전송에 오류가있습니다."),
	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 다릅니다."),
	NOT_EXIST_BOARD(HttpStatus.NOT_FOUND, "존재하지 않는 게시판"),
	NOT_EXIST_COMMENT(HttpStatus.NOT_FOUND, "존재하지 않는 댓글"),
	NOT_EXIST_NOTIFICATION(HttpStatus.NOT_FOUND, "존재하지 않는 알람"),
	INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "게시물 작성자가 아닙니다."),
	INVALID_PERMISSION_FAVORITE(HttpStatus.UNAUTHORIZED, "좋아요를 누른 사용자가 아닙니다."),
	INVALID_PERMISSION_COMMENT(HttpStatus.UNAUTHORIZED, "댓글 수정 권한이 없습니다.");

	public final HttpStatus status;
	private final String msg;

	ErrorCode(HttpStatus status, String msg) {
		this.status = status;
		this.msg = msg;
	}
}
