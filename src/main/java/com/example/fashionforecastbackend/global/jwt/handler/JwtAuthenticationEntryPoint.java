package com.example.fashionforecastbackend.global.jwt.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.fashionforecastbackend.global.jwt.service.ErrorResponseMaker;
import com.example.fashionforecastbackend.global.response.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final ErrorResponseMaker errorResponseMaker;

	@Override
	public void commence(HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException authException) throws IOException {
		// 유효한 자격증명을 제공하지 않고 접근하려 할때 401 에러 리턴
		setErrorResponse(request, response, authException);
	}

	private void setErrorResponse(HttpServletRequest request, HttpServletResponse response, Exception e) throws
		IOException {
		Map<String, String> error = new HashMap<>();
		error.put("path", request.getRequestURI());
		ErrorResponse<String> errorResponse = ErrorResponse.error(error, HttpStatus.UNAUTHORIZED.toString(),
			e.getMessage());
		errorResponseMaker.mapToJson(response, errorResponse, HttpServletResponse.SC_UNAUTHORIZED);
	}
}

