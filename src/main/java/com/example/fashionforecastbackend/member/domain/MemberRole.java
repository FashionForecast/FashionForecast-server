package com.example.fashionforecastbackend.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRole {

	MEMBER("ROLE_USER");

	private final String key;
}
