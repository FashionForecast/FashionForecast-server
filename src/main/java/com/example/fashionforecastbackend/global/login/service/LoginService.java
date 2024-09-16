package com.example.fashionforecastbackend.global.login.service;

import com.example.fashionforecastbackend.global.login.dto.response.AccessTokenResponse;

public interface LoginService {

	String renewAccessToken(String refreshTokenRequest, final String authorizationHeader);
	void removeRefreshToken(final String refreshTokenRequest);
	AccessTokenResponse issueAccessToken(final String refreshTokenRequest);
}
