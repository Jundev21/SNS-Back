package com.sns.sns.service.domain.member.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRole {

    USER("NORMAL_USER"),
    ADMIN("ADMIN");


    private final String role;
}
