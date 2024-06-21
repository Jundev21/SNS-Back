package com.sns.sns.service.domain.member.dto.response;

import com.sns.sns.service.domain.member.model.UserRole;
import com.sns.sns.service.domain.member.model.entity.Member;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record RegisterResponse(
        Long id,
        String userLoginId,
        String userName,
        UserRole role
) {

    public static RegisterResponse fromEntity(Member member){
        return new RegisterResponse(
                member.getId(),
                member.getUserLoginId(),
                member.getUserName(),
                member.getRole()
        );

    }
}
