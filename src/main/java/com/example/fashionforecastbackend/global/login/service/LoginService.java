package com.example.fashionforecastbackend.global.login.service;

import com.example.fashionforecastbackend.global.login.domain.MemberTokens;
import com.example.fashionforecastbackend.global.login.dto.response.AccessTokenResponse;

import jakarta.servlet.http.HttpServletResponse;

public interface LoginService {

	MemberTokens renewTokens(final MemberTokens tokens, final HttpServletResponse response);

	void removeRefreshToken(final String refreshTokenRequest);

	AccessTokenResponse issueAccessToken(final String refreshTokenRequest);
}
