package com.sns.sns.service.jwt;

import com.sns.sns.service.domain.member.model.UserStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sns.sns.service.common.exception.BasicException;
import com.sns.sns.service.common.exception.ErrorCode;
import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {

	private final MemberRepository memberRepository;

	// 권한 요청할때마다 사용자가 유효한 사용자인지 디비를 거쳤다 간다.
	// 디비를 거치지않고 레디스 캐쉬를 사용해서 빠르게 확인할 수 있다.
	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
//		return memberRepository.findByUserLoginId(loginId)
//			.orElseThrow((() -> new BasicException(ErrorCode.NOT_EXIST_MEMBER, ErrorCode.NOT_EXIST_MEMBER.getMsg())));
		return memberRepository.findByUserLoginIdAndUserStatus(loginId, UserStatus.JOIN)
			.orElseThrow((() -> new BasicException(ErrorCode.NOT_EXIST_MEMBER, ErrorCode.NOT_EXIST_MEMBER.getMsg())));

	}
}
