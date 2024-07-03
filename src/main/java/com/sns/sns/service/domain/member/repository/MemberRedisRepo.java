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
	// time limit 걸어줘야함 왜냐하면 멤버가 더이상 서비스를 사용하지 않을 경우에는 메모리 낭비가 될 수 있기 때문
	private final Duration redisLimitTime = Duration.ofDays(7);

	public void setMember(Member member) {
		String key = getKey(member.getUserLoginId());
		log.info("set user key to redis,{},{} ", key, member);
		memberRedisTemplate.opsForValue().set(getKey(member.getUserLoginId()), member, redisLimitTime);
	}

	public Optional<Member> getMember(String userName) {

		String key = getKey(userName);
		Member member = memberRedisTemplate.opsForValue().get(getKey(userName));
		log.info("get user data to redis,{},{}", key, member);
		return Optional.ofNullable(member);
	}

	//prefix 를 추가해줘서 어떤 데이터를 저장하는 키인지 알려 줌
	private String getKey(String userName) {
		return "Member" + userName;
	}
}
