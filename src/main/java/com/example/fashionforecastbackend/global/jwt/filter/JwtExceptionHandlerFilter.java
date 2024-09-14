package com.example.fashionforecastbackend.global.jwt.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.fashionforecastbackend.global.error.GeneralException;
import com.example.fashionforecastbackend.global.jwt.service.ErrorResponseMaker;
import com.example.fashionforecastbackend.global.response.ErrorResponse;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

	private final ErrorResponseMaker errorResponseMaker;

	/**
	 * 토큰 관련 에러 핸들링
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (GeneralException e) {
			// 토큰
			setErrorResponse(request, response, e);
		}
	}

	private void setErrorResponse(HttpServletRequest request, HttpServletResponse response, GeneralException e) throws
		IOException {
		Map<String, String> error = new HashMap<>();
		error.put("path", request.getRequestURI());
		ErrorResponse<String> errorResponse = ErrorResponse.error(error, e.getErrorCode().toString(),
			e.getMessage());
		errorResponseMaker.mapToJson(response, errorResponse, HttpServletResponse.SC_UNAUTHORIZED);
	}
}
