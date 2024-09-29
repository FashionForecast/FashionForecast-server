package com.example.fashionforecastbackend.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRole {

	MEMBER("ROLE_USER"),
	ADMIN("ROLE_ADMIN");

	private final String key;
}
