package com.sns.sns.service.domain.member.repository;

import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.member.model.entity.MemberRedisEntity;
import org.springframework.data.repository.CrudRepository;

public interface MemberRedisRepository extends CrudRepository<MemberRedisEntity,String> {
}
