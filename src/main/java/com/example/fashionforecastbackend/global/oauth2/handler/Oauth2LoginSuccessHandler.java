package com.example.fashionforecastbackend.global.oauth2.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.fashionforecastbackend.global.jwt.service.JwtService;
import com.example.fashionforecastbackend.global.login.domain.RefreshToken;
import com.example.fashionforecastbackend.global.login.domain.repository.RefreshTokenRepository;
import com.example.fashionforecastbackend.global.oauth2.CustomOauth2User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2LoginSuccessHandler implements AuthenticationSuccessHandler {

	private final JwtService jwtService;
	private final RefreshTokenRepository refreshTokenRepository;

	@Value("${redirect.base-url}")
	private String baseUrl;

	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
		final Authentication authentication) throws IOException, ServletException {
		log.info("Oauth2 로그인 성공 : {}", authentication.getPrincipal());
		final CustomOauth2User principal = (CustomOauth2User)authentication.getPrincipal();
		final String memberId = String.valueOf(principal.getMemberId());
		//		final MemberTokens memberTokens = jwtService.generateLoginToken(response, memberId, principal.getRole());
		final String refreshToken = jwtService.generateRefreshToken(response, memberId, principal.getRole());
		saveRefreshTokenInRedis(refreshToken, memberId);
		response.setStatus(HttpServletResponse.SC_OK);


		/*
		  "forecast-test.shop" = 백엔드 DNS
		 */
		//		if (serverName.contains("forecast-test.shop")) {
		//			redirectUri = "https://fashion-forecast.pages.dev";
		//		}

		final String redirectUrl = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/login/auth")
			.queryParam("social-login", true)
			.build()
			.toString();

		// 회원 전용 페이지로 이동하게끔 수정 필요
		response.sendRedirect(redirectUrl);
	}

	private void saveRefreshTokenInRedis(final String refreshToken, final String memberId) {
		final RefreshToken redisRefreshToken = new RefreshToken(memberId, refreshToken);
		refreshTokenRepository.save(redisRefreshToken);
	}

}
