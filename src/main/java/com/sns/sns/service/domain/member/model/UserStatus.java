package com.sns.sns.service.domain.member.model;


import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum UserStatus {

    JOIN("회원"),
    WITHDRAW("회원탈퇴");

    private final String status;
}
