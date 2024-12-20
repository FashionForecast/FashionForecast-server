package com.example.fashionforecastbackend.global.login.service;

import com.example.fashionforecastbackend.global.login.dto.request.AccessTokenRequest;
import com.example.fashionforecastbackend.global.login.dto.response.AccessTokenResponse;

import jakarta.servlet.http.HttpServletResponse;

public interface LoginService {

	AccessTokenResponse renewTokens(final AccessTokenRequest request, final String refreshToken,
		final HttpServletResponse response);

	void removeRefreshToken(final Long memberId);

	void deleteAccount(final Long memberId, HttpServletResponse response);

	AccessTokenResponse issueAccessToken(final String refreshTokenRequest);
}
