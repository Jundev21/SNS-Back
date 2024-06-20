package com.sns.sns.service.domain.board.controller;


import com.sns.sns.service.common.response.Response;
import com.sns.sns.service.domain.board.dto.request.BoardRequest;
import com.sns.sns.service.domain.board.dto.request.BoardUpdateRequest;
import com.sns.sns.service.domain.board.dto.response.*;
import com.sns.sns.service.domain.board.service.BoardService;
import com.sns.sns.service.domain.member.model.entity.Member;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/board")
public class BoardController {

    private final BoardService  boardService;

    @PostMapping
    public Response<BoardResponse> writeBoard(
            @RequestBody BoardRequest boardRequest,
            @AuthenticationPrincipal Member member
            ){
        return Response.success(boardService.writeBoard(boardRequest,member));
    }

    @PutMapping("/{boardId}")
    public Response<BoardUpdateResponse> updateBoard(
            @PathVariable Long boardId,
            @RequestBody BoardUpdateRequest boardUpdateRequest,
            @AuthenticationPrincipal Member member
    ){
        return Response.success(boardService.updateBoard(boardId, boardUpdateRequest,member));
    }

    @DeleteMapping("/{boardId}")
    public Response<BoardDeleteResponse> deleteBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal Member member
    ){
        return Response.success(boardService.deleteBoard(boardId,member));
    }

    @GetMapping("/{boardId}")
    public Response<BoardDetailResponse> deleteBoard(
            @PathVariable Long boardId
    ){
        return Response.success(boardService.getBoardDetail(boardId));
    }

    @GetMapping
    public Response<Page<BoardGetResponse>> getBoard(
            Pageable pageable
    ){
        return Response.success(boardService.getBoard(pageable));
    }
    @GetMapping("/user")
    public Response<Page<BoardGetResponse>> getUserBoard(
            Pageable pageable, @AuthenticationPrincipal Member member
    ){
        return Response.success(boardService.getUserBoard(pageable, member));
    }



}
