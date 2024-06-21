package com.sns.sns.service.jwt;

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

	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
		Member member = memberRepository.findByMemberLoginId(loginId)
			.orElseThrow((() -> new BasicException(ErrorCode.NOT_EXIST_MEMBER, ErrorCode.NOT_EXIST_MEMBER.getMsg())));
		return new MemberDetail(member);
	}
}
