package com.sns.sns.service.repository;

import com.sns.sns.service.domain.member.model.UserStatus;
import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.member.repository.MemberRepository;
import com.sns.sns.service.domain.member.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("사용자 회원탈퇴 확인")
     void WidthDrawMember(){

        Member newMember = new Member(
                "testLoginId",
                "test userName",
                "password",
                "userEmail",
                "image",
                UserStatus.JOIN
        );

        Member savedNewMember =memberRepository.save(newMember);
        Assertions.assertEquals(savedNewMember.getUserLoginId(),"testLoginId");
        savedNewMember.UpdateMemberStatus(UserStatus.WITHDRAW);

        Assertions.assertEquals(savedNewMember.getUserStatus(),UserStatus.WITHDRAW);
        Member widthDrawMember = memberRepository.findByUserLoginIdAndUserStatus("testLoginId",UserStatus.WITHDRAW).orElseThrow();

        Assertions.assertEquals(widthDrawMember.getUserEmail(), "userEmail");



    }

}
