package com.sns.sns.service.data;

import com.sns.sns.service.domain.board.dto.request.BoardRequest;
import com.sns.sns.service.domain.board.dto.request.BoardUpdateRequest;
import com.sns.sns.service.domain.board.model.BoardEntity;
import com.sns.sns.service.domain.member.model.entity.Member;

public class BoardData {


    public static BoardUpdateRequest boardUpdateRequestData(){
        return new BoardUpdateRequest("update Request","update Request content");
    }

    public static BoardRequest boardRequestData(){
        return new BoardRequest("create new board title", "create new board contents");
    }

    public static BoardEntity updateBoard(BoardUpdateRequest boardUpdateRequest, Member member){

        return new BoardEntity(boardUpdateRequest.title(), boardUpdateRequest.content(), member);

    }

    public static BoardEntity createNewBoard(BoardRequest boardRequest, Member member){

        return new BoardEntity(boardRequest.title(), boardRequest.contents(), member);

    }
}
