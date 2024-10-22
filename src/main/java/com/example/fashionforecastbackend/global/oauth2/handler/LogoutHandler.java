package com.example.fashionforecastbackend.global.oauth2.handler;

import static com.example.fashionforecastbackend.global.error.ErrorCode.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.fashionforecastbackend.global.error.exception.NotFoundRefreshToken;
import com.example.fashionforecastbackend.global.jwt.service.JwtService;
import com.example.fashionforecastbackend.global.login.domain.repository.RefreshTokenRepository;
import com.example.fashionforecastbackend.global.oauth2.UserDetail;
import com.example.fashionforecastbackend.global.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LogoutHandler implements LogoutSuccessHandler {

	private static final String REFRESH_TOKEN = "refresh_token";
	private final JwtService jwtService;
	private final RefreshTokenRepository refreshTokenRepository;
	private final ObjectMapper mapper;

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		UserDetail principal = (UserDetail)authentication.getPrincipal();
		Long memberId = principal.memberId();

		String refreshToken = getRefreshToken(request);
		if (refreshToken == null) {
			throw new NotFoundRefreshToken(NOT_FOUND_REFRESH_TOKEN);
		}
		jwtService.validateRefreshToken(refreshToken);
		if (!refreshTokenRepository.existsById(String.valueOf(memberId))) {
			throw new NotFoundRefreshToken(NOT_FOUND_REFRESH_TOKEN);
		}
		refreshTokenRepository.deleteById(String.valueOf(memberId));
		deleteRefreshTokenCookie(response);

		convertToJson(response);
	}

	private String getRefreshToken(HttpServletRequest request) {
		String refreshToken = null;
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(REFRESH_TOKEN)) {
				refreshToken = cookie.getValue();
			}
		}
		return refreshToken;
	}

	private void deleteRefreshTokenCookie(HttpServletResponse response) {
		Cookie cookie = new Cookie(REFRESH_TOKEN, null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

	private void convertToJson(HttpServletResponse response) throws IOException {
		String convertedDto = mapper.writeValueAsString(ApiResponse.noContent());

		response.setStatus(HttpStatus.ACCEPTED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

		PrintWriter writer = response.getWriter();
		writer.write(convertedDto);
	}
}
