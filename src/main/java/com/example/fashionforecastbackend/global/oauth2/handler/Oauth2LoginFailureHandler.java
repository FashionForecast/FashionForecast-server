package com.example.fashionforecastbackend.global.oauth2.handler;

import static com.example.fashionforecastbackend.global.error.ErrorCode.*;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.example.fashionforecastbackend.global.response.ErrorResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Oauth2LoginFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response,
		final AuthenticationException exception) throws IOException, ServletException {

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter()
			.write(ErrorResponse.error(SOCIAL_LOGIN_FAILED.getCode(), SOCIAL_LOGIN_FAILED.getMessage()).toString());
		log.info("소셜 로그인 실패 : {}", exception.getMessage());
	}
}
