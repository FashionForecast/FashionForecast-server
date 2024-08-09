package com.example.fashionforecastbackend.global.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	/**
	 * Etc
	 */
	INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "E001", "잘못된 요청입니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}
