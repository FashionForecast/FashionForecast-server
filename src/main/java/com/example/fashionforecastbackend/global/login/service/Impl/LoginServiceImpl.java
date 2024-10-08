package com.example.fashionforecastbackend.global.login.service.Impl;

import static com.example.fashionforecastbackend.global.error.ErrorCode.*;
import static com.example.fashionforecastbackend.member.domain.constant.MemberRole.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fashionforecastbackend.global.error.exception.InvalidJwtException;
import com.example.fashionforecastbackend.global.error.exception.NotFoundRefreshToken;
import com.example.fashionforecastbackend.global.jwt.service.JwtService;
import com.example.fashionforecastbackend.global.login.domain.MemberTokens;
import com.example.fashionforecastbackend.global.login.domain.RefreshToken;
import com.example.fashionforecastbackend.global.login.domain.repository.RefreshTokenRepository;
import com.example.fashionforecastbackend.global.login.dto.request.AccessTokenRequest;
import com.example.fashionforecastbackend.global.login.dto.response.AccessTokenResponse;
import com.example.fashionforecastbackend.global.login.service.LoginService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * TODO.1 리프레시 토큰 삭제 구현
 * TODO.2 명시적 로그아웃 로직 구현
 */
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

	private final JwtService jwtService;
	private final RefreshTokenRepository refreshTokenRepository;

	@Transactional
	@Override
	public AccessTokenResponse renewTokens(final AccessTokenRequest request, final String refreshToken,
		final HttpServletResponse response) {
		String accessToken = request.accessToken();
		jwtService.validateRefreshToken(refreshToken);

		String memberId = jwtService.getSubject(accessToken);
		String role = jwtService.getRole(accessToken);

		RefreshToken savedRefreshToken = refreshTokenRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundRefreshToken(NOT_FOUND_REFRESH_TOKEN));

		compareRefreshToken(refreshToken, savedRefreshToken.getToken());

		MemberTokens newTokens = jwtService.generateLoginToken(response, memberId, role);

		savedRefreshToken.updateToken(newTokens.getRefreshToken());
		refreshTokenRepository.save(savedRefreshToken);

		return AccessTokenResponse.of(newTokens.getAccessToken());
	}

	@Override
	public void removeRefreshToken(final String refreshTokenRequest) {

	}

	@Override
	public AccessTokenResponse issueAccessToken(final String refreshTokenRequest) {
		final String memberId = jwtService.validateRefreshToken(refreshTokenRequest);
		final String accessToken = jwtService.generateAccessToken(memberId, MEMBER.getKey());

		return AccessTokenResponse.of(accessToken);

	}

	private void compareRefreshToken(String refreshToken, String savedRefreshToken) {
		if (!refreshToken.equals(savedRefreshToken)) {
			throw new InvalidJwtException(NOT_MATCH_REFRESH_TOKEN);
		}
	}

}
