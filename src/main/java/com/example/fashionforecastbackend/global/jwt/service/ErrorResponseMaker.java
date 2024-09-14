package com.example.fashionforecastbackend.global.jwt.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ErrorResponseMaker {

	private final ObjectMapper mapper;

	public void mapToJson(HttpServletResponse response, Object errorResponse, int status) throws
		IOException {
		String convertedDto = mapper.writeValueAsString(errorResponse);

		response.setStatus(status);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

		PrintWriter writer = response.getWriter();
		writer.write(convertedDto);
	}
}
