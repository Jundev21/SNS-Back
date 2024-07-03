package com.sns.sns.service.common;

public enum UserRoles {
	USER("user"),
	ADMIN("admin");

	private final String role;

	UserRoles(String role){
		this.role = role;
	}

}
