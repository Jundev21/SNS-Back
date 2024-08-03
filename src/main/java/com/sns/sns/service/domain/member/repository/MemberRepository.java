package com.sns.sns.service.domain.member.repository;

import com.sns.sns.service.domain.member.model.UserStatus;
import com.sns.sns.service.domain.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.management.Notification;
import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByUserLoginId(String loginId);
    Optional<Member> findByUserLoginIdAndUserStatus(String loginId, UserStatus userStatus);
    Optional<Member> findByUserName(String userName);
    Boolean existsByUserLoginId(String loginId);

}
