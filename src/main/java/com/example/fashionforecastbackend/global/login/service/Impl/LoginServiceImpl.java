package com.example.fashionforecastbackend.global.login.service.Impl;

import com.example.fashionforecastbackend.global.login.service.LoginService;

/**
 * TODO.1 액세스 토큰 재발급 구현
 * TODO.2 리프레시 토큰 삭제 구현
 * TODO.3 명시적 로그아웃 로직 구현
 */
public class LoginServiceImpl implements LoginService {


	@Override
	public String renewAccessToken(final String refreshTokenRequest, final String authorizationHeader) {
		return "";
	}

	@Override
	public void removeRefreshToken(final String refreshTokenRequest) {

	}
}
