package com.example.fashionforecastbackend.global.login.service;

public interface LoginService {

	String renewAccessToken(final String refreshTokenRequest, final String authorizationHeader);
	void removeRefreshToken(final String refreshTokenRequest);

}
