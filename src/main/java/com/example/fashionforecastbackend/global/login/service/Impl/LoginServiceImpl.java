package com.example.fashionforecastbackend.global.login.service.Impl;

import org.springframework.stereotype.Service;

import com.example.fashionforecastbackend.global.jwt.service.JwtService;
import com.example.fashionforecastbackend.global.login.dto.request.AccessTokenRequest;
import com.example.fashionforecastbackend.global.login.dto.response.AccessTokenResponse;
import com.example.fashionforecastbackend.global.login.service.LoginService;
import com.example.fashionforecastbackend.member.domain.MemberRole;

import lombok.RequiredArgsConstructor;

/**
 * TODO.1 액세스 토큰 재발급 구현
 * TODO.2 리프레시 토큰 삭제 구현
 * TODO.3 명시적 로그아웃 로직 구현
 */
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

	private final JwtService jwtService;


	@Override
	public String renewAccessToken(final String refreshTokenRequest, final String authorizationHeader) {
		return "";
	}

	@Override
	public void removeRefreshToken(final String refreshTokenRequest) {

	}

	@Override
	public AccessTokenResponse issueAccessToken(final AccessTokenRequest request) {
		final String accessToken = jwtService.generateAccessToken(String.valueOf(request.memberId()),
			MemberRole.MEMBER.getKey());
		return AccessTokenResponse.of(accessToken);
	}
}
