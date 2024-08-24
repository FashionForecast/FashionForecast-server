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
	INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "E001", "잘못된 요청입니다."),

	/**
	 * 날씨
	 */
	INVALID_WEATHER_NOW_DATE_TIME(HttpStatus.BAD_REQUEST, "W001", "날씨 조회를 위한 현재 시간은 과거일 수 없습니다."),
	INVALID_WEATHER_START_DATE_TIME(HttpStatus.BAD_REQUEST, "W002",
		"외출 시작 시간은 현재 시간 보다 과거일 수 없고, 외출 끝 시간 보다 미래일 수 없습니다."),
	INVALID_WEATHER_END_DATE_TIME(HttpStatus.BAD_REQUEST, "W003", "외출 끝 시간은 현재 시간 또는 외출 시작 시간 보다 과거일 수 없습니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}
