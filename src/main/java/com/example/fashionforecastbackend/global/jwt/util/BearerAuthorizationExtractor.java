package com.example.fashionforecastbackend.global.jwt.util;

import static com.example.fashionforecastbackend.global.error.ErrorCode.*;

import org.springframework.stereotype.Component;

import com.example.fashionforecastbackend.global.error.exception.InvalidJwtException;

@Component
public class BearerAuthorizationExtractor {

	private static final String BEARER_TYPE = "Bearer ";

	public String extractAccessToken(String header) {

		if (header == null || header.startsWith(BEARER_TYPE)) {
			return header.substring(BEARER_TYPE.length()).trim();
		}
		throw new InvalidJwtException(INVALID_ACCESS_TOKEN);

	}
}
