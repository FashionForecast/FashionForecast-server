package com.example.fashionforecastbackend.global.login.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MemberTokens {
	@NotNull(message = "리프레시 토큰을 입력해주세요.")
	private final String refreshToken;
	@NotNull(message = "액세스 토큰을 입력해주세요.")
	private final String accessToken;
}
