package com.sns.sns.service.jwt;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.sns.sns.service.domain.member.model.entity.Member;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MemberDetail implements UserDetails {

	private final Member memberEntity;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority(this.memberEntity.getRole().toString()));
	}

	@Override
	public String getPassword() {
		return memberEntity.getPassword();
	}

	@Override
	public String getUsername() {
		return this.memberEntity.getUserLoginId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
