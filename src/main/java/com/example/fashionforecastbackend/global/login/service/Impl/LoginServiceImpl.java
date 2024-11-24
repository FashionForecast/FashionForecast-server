package com.example.fashionforecastbackend.global.login.service.Impl;

import static com.example.fashionforecastbackend.global.error.ErrorCode.*;
import static com.example.fashionforecastbackend.member.domain.constant.MemberRole.*;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fashionforecastbackend.global.error.ErrorCode;
import com.example.fashionforecastbackend.global.error.exception.InvalidJwtException;
import com.example.fashionforecastbackend.global.error.exception.MemberNotFoundException;
import com.example.fashionforecastbackend.global.error.exception.NotFoundRefreshToken;
import com.example.fashionforecastbackend.global.jwt.service.JwtService;
import com.example.fashionforecastbackend.global.login.domain.MemberTokens;
import com.example.fashionforecastbackend.global.login.domain.RefreshToken;
import com.example.fashionforecastbackend.global.login.domain.repository.RefreshTokenRepository;
import com.example.fashionforecastbackend.global.login.dto.request.AccessTokenRequest;
import com.example.fashionforecastbackend.global.login.dto.response.AccessTokenResponse;
import com.example.fashionforecastbackend.global.login.service.LoginService;
import com.example.fashionforecastbackend.member.domain.Member;
import com.example.fashionforecastbackend.member.domain.MemberDeleteEvent;
import com.example.fashionforecastbackend.member.domain.repository.MemberRepository;
import com.example.fashionforecastbackend.search.service.SearchService;

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
	private final ApplicationEventPublisher eventPublisher;
	private final MemberRepository memberRepository;
	private final SearchService searchService;

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
	public void removeRefreshToken(final Long memberId) {
		validateExistMember(memberId);
		refreshTokenRepository.deleteById(String.valueOf(memberId));
	}

	@Override
	public AccessTokenResponse issueAccessToken(final String refreshTokenRequest) {
		final String memberId = jwtService.validateRefreshToken(refreshTokenRequest);
		final String accessToken = jwtService.generateAccessToken(memberId, MEMBER.getKey());

		return AccessTokenResponse.of(accessToken);

	}

	@Transactional
	@Override
	public void deleteAccount(final Long memberId, HttpServletResponse response) {
		validateExistMember(memberId);
		removeRefreshToken(memberId);
		deleteAllSearch(memberId);
		eventPublisher.publishEvent(MemberDeleteEvent.of(memberId));
		deleteRefreshToken(response);
	}

	private void deleteAllSearch(final Long memberId) {
		final Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberNotFoundException(ErrorCode.NOT_FOUND_MEMBER));
		searchService.deleteAllSearch(member.getSocialId());
	}

	private void deleteRefreshToken(HttpServletResponse response) {
		ResponseCookie cookie = ResponseCookie.from("refresh_token", "")
			.maxAge(0)
			.sameSite("Lax")
			.secure(true)
			.httpOnly(true)
			.path("/")
			.build();

		response.setHeader("Set-Cookie", cookie.toString());
	}

	private void validateExistMember(final Long memberId) {
		if (!memberRepository.existsById(memberId)) {
			throw new MemberNotFoundException(NOT_FOUND_MEMBER);
		}
	}

	private void compareRefreshToken(String refreshToken, String savedRefreshToken) {
		if (!refreshToken.equals(savedRefreshToken)) {
			throw new InvalidJwtException(NOT_MATCH_REFRESH_TOKEN);
		}
	}

}
