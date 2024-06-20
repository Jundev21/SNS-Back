package com.sns.sns.service.service;


import com.sns.sns.service.domain.board.model.BoardEntity;
import com.sns.sns.service.domain.board.repository.BoardRepository;
import com.sns.sns.service.domain.comment.dto.request.CommentPostRequest;
import com.sns.sns.service.domain.comment.dto.response.CommentGetResponse;
import com.sns.sns.service.domain.comment.dto.response.CommentPostResponse;
import com.sns.sns.service.domain.comment.dto.response.CommentUpdateResponse;
import com.sns.sns.service.domain.comment.model.CommentEntity;
import com.sns.sns.service.domain.comment.repository.CommentRepository;
import com.sns.sns.service.domain.comment.service.CommentService;
import com.sns.sns.service.domain.exception.BasicException;
import com.sns.sns.service.domain.member.dto.response.BasicUserInfoResponse;
import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Optional;

import static com.sns.sns.service.data.BoardData.boardRequestData;
import static com.sns.sns.service.data.BoardData.createNewBoard;
import static com.sns.sns.service.data.CommentData.commentPostRequestData;
import static com.sns.sns.service.data.CommentData.commentUpdateRequest;
import static com.sns.sns.service.data.UserData.userEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private BoardRepository boardRepository;

    // 댓글 작성하기 위해서는 해당 사용자가 존재하는 사용자인지 확인. (권한여부)
    // 게시판이 있는지 확인 없으면 댓글 추가 불가
    // 만약 사용자가 확인이 되면 데이터 내용을 바로 comment 로 저장.

    @Test
    @WithMockUser
    @DisplayName("댓글 작성 성공")
    public void successToWriteComment() throws Exception {

        Member member = userEntity("test user name", "member password");
        BoardEntity boardEntity = createNewBoard(boardRequestData(), member);
        CommentPostRequest commentPostRequest = commentPostRequestData();
        CommentEntity newComment = new CommentEntity("new comment", boardEntity,member);

        when(memberRepository.findByUserName(any())).thenReturn(Optional.of(member));
        when(boardRepository.findById(anyLong())).thenReturn(Optional.of(boardEntity));
        when(commentRepository.save(any())).thenReturn(newComment);

//        CommentPostResponse result = commentService.createComment(1L,member,commentPostRequest);

//        assertThat(result.commentInfo().commentId()).isEqualTo(boardEntity.getId());
//        assertThat(result.userInfo()).isEqualTo(BasicUserInfoResponse.basicUserInfoResponse(member));
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 조회 성공")
    public void successToGetComment() throws Exception {

        Member member = userEntity("test user name", "member password");
        BoardEntity boardEntity = createNewBoard(boardRequestData(), member);
        CommentPostRequest commentPostRequest = commentPostRequestData();
        List<CommentEntity> newComment =
                List.of(
                        new CommentEntity("first new comment", boardEntity,member),
                        new CommentEntity("second new comment", boardEntity,member)
                );

        when(memberRepository.findByUserName(any())).thenReturn(Optional.of(member));
        when(boardRepository.findById(anyLong())).thenReturn(Optional.of(boardEntity));
        when(commentRepository.findAllCommentEntityByBoardEntity(any())).thenReturn(newComment);

        List<CommentGetResponse> result = commentService.getComment(1L,member);

        System.out.println(result);
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 수정 성공")
    public void successToUpdateComment() throws Exception {

        Member member = userEntity("test user name", "member password");
        BoardEntity boardEntity = createNewBoard(boardRequestData(), member);
        CommentPostRequest commentPostRequest = commentPostRequestData();
        CommentEntity newComment = new CommentEntity("first new comment", boardEntity,member);


        when(memberRepository.findByUserName(any())).thenReturn(Optional.of(member));
        when(boardRepository.findById(anyLong())).thenReturn(Optional.of(boardEntity));
        when(commentRepository.findById(any())).thenReturn(Optional.of(newComment));

        CommentUpdateResponse result = commentService.updateComment(1L,1L,member, commentUpdateRequest());

        assertThat(result.commentInfo().content()).isEqualTo(commentUpdateRequest().content());
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 수정 권한이 없어 수정 실패")
    public void failToUpdateComment() throws Exception {

        Member member = userEntity("test user name", "member password");
        Member otherMember = userEntity("other Member", "password");
        BoardEntity boardEntity = createNewBoard(boardRequestData(), member);
        CommentPostRequest commentPostRequest = commentPostRequestData();
        CommentEntity newComment = new CommentEntity("first new comment", boardEntity,otherMember);

        when(memberRepository.findByUserName(any())).thenReturn(Optional.of(member));
        when(boardRepository.findById(anyLong())).thenReturn(Optional.of(boardEntity));
        when(commentRepository.findById(any())).thenReturn(Optional.of(newComment));

        assertThrows(BasicException.class, () -> commentService.updateComment(1L, 1L, member, commentUpdateRequest()));
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 삭제 성공")
    public void deleteComment() throws Exception {

        Member member = userEntity("test user name", "member password");
        BoardEntity boardEntity = createNewBoard(boardRequestData(), member);
        CommentPostRequest commentPostRequest = commentPostRequestData();
        CommentEntity newComment = new CommentEntity("first new comment", boardEntity,member);

        when(memberRepository.findByUserName(any())).thenReturn(Optional.of(member));
        when(boardRepository.findById(anyLong())).thenReturn(Optional.of(boardEntity));
        when(commentRepository.findById(any())).thenReturn(Optional.of(newComment));

        assertDoesNotThrow(()->commentService.deleteComment(1L,1L,member));
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 삭제 권한이 없어 실패")
    public void deleteAuthComment() throws Exception {

        Member member = userEntity("test user name", "member password");
        Member otherMember = userEntity("other Member", "password");

        BoardEntity boardEntity = createNewBoard(boardRequestData(), member);
        CommentPostRequest commentPostRequest = commentPostRequestData();
        CommentEntity newComment = new CommentEntity("first new comment", boardEntity,otherMember);

        when(memberRepository.findByUserName(any())).thenReturn(Optional.of(member));
        when(boardRepository.findById(anyLong())).thenReturn(Optional.of(boardEntity));
        when(commentRepository.findById(any())).thenReturn(Optional.of(newComment));

        assertThrows(BasicException.class,()-> commentService.deleteComment(1L, 1L, member));
    }
}
