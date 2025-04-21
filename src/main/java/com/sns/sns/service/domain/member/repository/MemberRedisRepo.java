package com.sns.sns.service.domain.member.repository;

import java.time.Duration;
import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.sns.sns.service.domain.member.model.entity.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRedisRepo {

	private final RedisTemplate<String, Member> memberRedisTemplate;
	private final Duration redisLimitTime = Duration.ofDays(7);

	public void setMember(Member member) {
		String key = getKey(member.getUserLoginId());
		log.info("set user key to redis,{},{} ", key, member);
		memberRedisTemplate.opsForValue().set(getKey(member.getUserLoginId()), member, redisLimitTime);
	}

	public Optional<Member> getMember(String loginId) {
		String key = getKey(loginId);
		Member member = memberRedisTemplate.opsForValue().get(getKey(loginId));
		log.info("get user data to redis,{},{}", key, member);
		return Optional.ofNullable(member);
	}

	private String getKey(String loginId) {
		return "Member loginId " + loginId;
	}
}
