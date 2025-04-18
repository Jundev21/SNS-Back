package com.sns.sns.service.domain.member.repository;

import com.sns.sns.service.domain.member.model.UserStatus;
import com.sns.sns.service.domain.member.model.entity.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MemberRedisRepository extends CrudRepository<Member,String> {
    Optional<Member> findByUserLoginIdAndUserStatus(String loginId, UserStatus userStatus);
}
