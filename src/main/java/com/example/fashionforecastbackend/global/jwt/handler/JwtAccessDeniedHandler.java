package com.example.fashionforecastbackend.global.jwt.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.example.fashionforecastbackend.global.jwt.service.ErrorResponseMaker;
import com.example.fashionforecastbackend.global.response.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

	private final ErrorResponseMaker errorResponseMaker;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException {
		//필요한 권한 없이 접근하려 할때 403 리턴
		setErrorResponse(request, response, accessDeniedException);
	}

	private void setErrorResponse(HttpServletRequest request, HttpServletResponse response, Exception e) throws
		IOException {
		Map<String, String> error = new HashMap<>();
		error.put("path", request.getRequestURI());
		ErrorResponse<String> errorResponse = ErrorResponse.error(error, HttpStatus.FORBIDDEN.toString(),
			e.getMessage());
		errorResponseMaker.mapToJson(response, errorResponse, HttpServletResponse.SC_FORBIDDEN);
	}
}

