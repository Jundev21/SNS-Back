package com.sns.sns.service.domain.member.service;


import com.sns.sns.service.domain.board.model.BoardEntity;
import com.sns.sns.service.domain.exception.BasicException;
import com.sns.sns.service.domain.exception.ErrorCode;
import com.sns.sns.service.domain.member.dto.request.LoginRequest;
import com.sns.sns.service.domain.member.dto.request.MemberUpdateRequest;
import com.sns.sns.service.domain.member.dto.request.RegisterRequest;
import com.sns.sns.service.domain.member.dto.response.LoginResponse;
import com.sns.sns.service.domain.member.dto.response.MemberInfoResponse;
import com.sns.sns.service.domain.member.dto.response.RegisterResponse;
import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.member.repository.MemberRedisRepo;
import com.sns.sns.service.domain.member.repository.MemberRepository;
import com.sns.sns.service.domain.notification.dto.NotificationType;
import com.sns.sns.service.domain.notification.model.NotificationEntity;
import com.sns.sns.service.domain.notification.repository.NotificationRepository;
import com.sns.sns.service.domain.notification.service.NotificationService;
import com.sns.sns.service.jwt.JwtTokenUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final MemberRedisRepo memberRedisRepo;

    @Transactional
    public RegisterResponse memberRegister(RegisterRequest registerRequest) {
        memberRepository.findByUserName(registerRequest.userName()).ifPresent(
                member->{
                    throw new BasicException(ErrorCode.ALREADY_EXIST_MEMBER, "이미 있는 회원입니다.");
                }
        );

        Member newMember = memberRepository.save(
                new Member(
                        registerRequest.userName(),
                        encoder.encode(registerRequest.password()),
                        registerRequest.userEmail()

                ));

        return RegisterResponse.fromEntity(newMember);
    }

    @Transactional(readOnly = true)
    public LoginResponse memberLogin(LoginRequest loginRequest) {

        //로컬 레디스
        Member member = memberRepository.findByUserName(loginRequest.userName())
                .orElseThrow(() -> new BasicException(ErrorCode.NOT_EXIST_MEMBER, "존재하지 않는 회원"));

//        Member member = loadMemberByMemberName(loginRequest.userName());
//        memberRedisRepo.setMember(member);

        if(!encoder.matches(loginRequest.password(), member.getPassword())){
            throw new BasicException(ErrorCode.INVALID_PASSWORD,"비밀번호 다릅니다.");
        }

        member.UpdateVisitedCount();

        String jwtToken = jwtTokenUtil.generateToken(member.getUsername());

        return new LoginResponse(jwtToken, null);
    }

    @Transactional(readOnly = true)
    public Member loadMemberByMemberName(String userName){

        //레디스 처리 해당 사용자가 존재하는지 매번 디비에서 확인하지않고 레디스에서 먼저 확인한다.
// //로컬 레디스
        return memberRepository.findByUserName(userName)
                .orElseThrow(() -> new BasicException(ErrorCode.NOT_EXIST_MEMBER, "존재하지 않는 회원"));

//        return memberRedisRepo.getMember(userName).orElseGet(() ->
//                memberRepository.findByUserName(userName)
//                        .orElseThrow(() -> new BasicException(ErrorCode.NOT_EXIST_MEMBER, "존재하지 않는 회원")));


    }
    @Transactional
    public MemberInfoResponse memberUpdate(MemberUpdateRequest memberUpdateRequest, Member member) {
        Member findMember = memberRepository.findByUserName(member.getUsername())
                .orElseThrow(() -> new BasicException(ErrorCode.NOT_EXIST_MEMBER, "존재하지 않는 회원"));

       findMember.UpdateMemberInfo(memberUpdateRequest.userEmail(), encoder.encode(memberUpdateRequest.password()));
        return MemberInfoResponse.memberInfo(findMember);

    }
    @Transactional(readOnly = true)
    public MemberInfoResponse getMemberInfo(Member member) {

        Member findMember = memberRepository.findByUserName(member.getUsername())
                .orElseThrow(() -> new BasicException(ErrorCode.NOT_EXIST_MEMBER, "존재하지 않는 회원"));
        return MemberInfoResponse.memberInfo(findMember);
    }
    @Transactional
    public void deleteMember(Member member) {
        Member findMember = memberRepository.findByUserName(member.getUsername())
                .orElseThrow(() -> new BasicException(ErrorCode.NOT_EXIST_MEMBER, "존재하지 않는 회원"));

        memberRepository.delete(findMember);

    }
}
