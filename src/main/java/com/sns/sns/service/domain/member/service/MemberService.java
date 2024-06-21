package com.sns.sns.service.domain.member.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sns.sns.service.common.exception.BasicException;
import com.sns.sns.service.common.exception.ErrorCode;
import com.sns.sns.service.domain.member.dto.request.LoginRequest;
import com.sns.sns.service.domain.member.dto.request.MemberUpdateRequest;
import com.sns.sns.service.domain.member.dto.request.RegisterRequest;
import com.sns.sns.service.domain.member.dto.response.LoginResponse;
import com.sns.sns.service.domain.member.dto.response.MemberInfoResponse;
import com.sns.sns.service.domain.member.dto.response.RegisterResponse;
import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.member.repository.MemberRepository;
import com.sns.sns.service.jwt.JwtTokenInfo;
import com.sns.sns.service.jwt.MemberDetail;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder encoder;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenInfo jwtTokenInfo;

	@Transactional
	public RegisterResponse memberRegister(RegisterRequest registerRequest) {
		checkExistMember(registerRequest.userLoginId());
		checkPassWordValidation(registerRequest.password(), registerRequest.checkPassword());
		Member newMember = memberRepository.save(
			new Member(
				registerRequest.userLoginId(),
				registerRequest.userName(),
				encoder.encode(registerRequest.password()),
				registerRequest.userEmail()
			));
		return RegisterResponse.fromEntity(newMember);
	}

	@Transactional(readOnly = true)
	public LoginResponse memberLogin(LoginRequest loginRequest) {
		//로컬 레디스 저장
		//Member member = loadMemberByMemberName(loginRequest.userName());
		// memberRedisRepo.setMember(member);
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				loginRequest.userLoginId(),
				loginRequest.password()
			)
		);
		MemberDetail memberDetail = (MemberDetail)authentication.getPrincipal();
		memberDetail.UpdateVisitedCount();
		String generatedJwtToken = jwtTokenInfo.generateToken(memberDetail.getUsername());
		return new LoginResponse(generatedJwtToken, null);
	}

	@Transactional
	public MemberInfoResponse memberUpdate(MemberUpdateRequest memberUpdateRequest, Member member) {
		notValidMember(member.getUserLoginId());
		member.UpdateMemberInfo(memberUpdateRequest.userEmail(), encoder.encode(memberUpdateRequest.password()));
		return MemberInfoResponse.memberInfo(member);

	}

	@Transactional(readOnly = true)
	public MemberInfoResponse getMemberInfo(Member member) {
		notValidMember(member.getUserLoginId());
		return MemberInfoResponse.memberInfo(member);
	}

	@Transactional
	public void deleteMember(Member member) {
		notValidMember(member.getUserLoginId());
		memberRepository.delete(member);

	}

	@Transactional(readOnly = true)
	public void checkExistMember(String loginId) {
		Boolean isExistMember = memberRepository.existsByMemberLoginId(loginId);
		if (isExistMember) {
			throw new BasicException(ErrorCode.ALREADY_EXIST_MEMBER, ErrorCode.ALREADY_EXIST_MEMBER.getMsg());
		}
	}

	@Transactional(readOnly = true)
	public void notValidMember(String loginId) {
		Boolean isExistMember = memberRepository.existsByMemberLoginId(loginId);
		if (!isExistMember) {
			throw new BasicException(ErrorCode.NOT_EXIST_MEMBER, ErrorCode.NOT_EXIST_MEMBER.getMsg());
		}
	}


	@Transactional(readOnly = true)
	public void checkPassWordValidation(String originPwd, String checkPwd) {
		if (!originPwd.equals(checkPwd)) {
			throw new BasicException(ErrorCode.MISS_MATCH_PWD, ErrorCode.MISS_MATCH_PWD.getMsg());
		}
	}
}

