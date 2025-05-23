package com.sns.sns.service.domain.member.service;

import com.sns.sns.service.domain.member.model.UserStatus;
import com.sns.sns.service.domain.member.repository.MemberRedisRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sns.sns.service.common.exception.BasicException;
import com.sns.sns.service.common.exception.ErrorCode;
import com.sns.sns.service.configuration.GoogleDriveConfig;
import com.sns.sns.service.domain.member.dto.request.LoginRequest;
import com.sns.sns.service.domain.member.dto.request.MemberUpdateRequest;
import com.sns.sns.service.domain.member.dto.request.RegisterRequest;
import com.sns.sns.service.domain.member.dto.response.LoginResponse;
import com.sns.sns.service.domain.member.dto.response.MemberInfoResponse;
import com.sns.sns.service.domain.member.dto.response.RegisterResponse;
import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.member.repository.MemberRepository;
import com.sns.sns.service.jwt.JwtTokenInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final GoogleDriveConfig googleDriveConfig;
	private final BCryptPasswordEncoder encoder;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenInfo jwtTokenInfo;
	private final MemberRedisRepo memberRedisRepo;

	@Transactional
	public RegisterResponse memberRegister(RegisterRequest registerRequest) {
		checkExistMember(registerRequest.userLoginId());
		checkPassWordValidation(registerRequest.password(), registerRequest.checkPassword());
		Member newMember = memberRepository.save(
			new Member(
				registerRequest.userLoginId(),
				registerRequest.userName(),
				encoder.encode(registerRequest.password()),
				registerRequest.userEmail(),
				"",
					UserStatus.JOIN
			));
		return RegisterResponse.fromEntity(newMember);
	}

	@Transactional
	public LoginResponse memberLogin(LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				loginRequest.userLoginId(),
				loginRequest.password()
			)
		);
		Member memberDetail = (Member)authentication.getPrincipal();
		memberDetail.UpdateVisitedCount();
		String generatedJwtToken = jwtTokenInfo.generateToken(memberDetail.getUserLoginId());
		return new LoginResponse(generatedJwtToken, null);
	}

	@Transactional
	public MemberInfoResponse memberUpdate(MemberUpdateRequest memberUpdateRequest, MultipartFile image,
		Member member) {
		Member findMember = notValidMember(member.getUserLoginId());
		String imageUrl = googleDriveConfig.uploadImageToGoogleDrive(image);
		if (imageUrl.isEmpty()) {
			imageUrl = findMember.getUserProfileImgUrl();
		}
		Member updatedMember = findMember.UpdateMemberInfo(memberUpdateRequest.userEmail(), encoder.encode(memberUpdateRequest.password()),
			imageUrl);
		memberRedisRepo.setMember(updatedMember);
		return MemberInfoResponse.memberInfo(updatedMember);
	}

	@Transactional
	public void softDeleteMember(Member member) {
		Member findMember = notValidMember(member.getUserLoginId());
		findMember.UpdateMemberStatus(UserStatus.WITHDRAW);
	}

	@Transactional
	public void hardDeleteMember(Member member) {
		notValidMember(member.getUserLoginId());
		memberRepository.delete(member);
	}

	@Transactional(readOnly = true)
	public MemberInfoResponse getMemberInfo(Member member) {
		notValidMember(member.getUserLoginId());
		return MemberInfoResponse.memberInfo(member);
	}

	@Transactional(readOnly = true)
	public void checkExistMember(String loginId) {
		Boolean isExistMember = memberRepository.existsByUserLoginId(loginId);
		if (isExistMember) {
			throw new BasicException(ErrorCode.ALREADY_EXIST_MEMBER, ErrorCode.ALREADY_EXIST_MEMBER.getMsg());
		}
	}

	@Transactional(readOnly = true)
	public Member notValidMember(String loginId) {
 		return memberRepository.findByUserLoginIdAndUserStatus(loginId, UserStatus.JOIN)
			.orElseThrow((() -> new BasicException(ErrorCode.NOT_EXIST_MEMBER, ErrorCode.NOT_EXIST_MEMBER.getMsg())));
	}

	@Transactional(readOnly = true)
	public void checkPassWordValidation(String originPwd, String checkPwd) {
		if (!originPwd.equals(checkPwd)) {
			throw new BasicException(ErrorCode.MISS_MATCH_PWD, ErrorCode.MISS_MATCH_PWD.getMsg());
		}
	}
}

