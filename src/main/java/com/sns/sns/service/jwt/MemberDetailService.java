package com.sns.sns.service.jwt;

import com.sns.sns.service.common.exception.BasicException;
import com.sns.sns.service.common.exception.ErrorCode;
import com.sns.sns.service.domain.member.model.UserStatus;
import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.member.repository.MemberRedisRepo;
import com.sns.sns.service.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberDetailService implements UserDetailsService {

	private final MemberRepository memberRepository;
	private final MemberRedisRepo memberRedisRepo;

	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        log.info("Redis 데이터 조회 - loadUserByUsername loginId:{}", loginId);
		return memberRedisRepo.getMember(loginId).orElseGet(
				() -> {
					log.info("DB 데이터 조회중 - loadUserByUsername loginId:{}", loginId);
					Member getMemberFromDB = memberRepository.findByUserLoginIdAndUserStatus(loginId, UserStatus.JOIN)
							.orElseThrow((() -> new BasicException(ErrorCode.NOT_EXIST_MEMBER, ErrorCode.NOT_EXIST_MEMBER.getMsg())));
					memberRedisRepo.setMember(getMemberFromDB);
					return getMemberFromDB;
				});
	}
}