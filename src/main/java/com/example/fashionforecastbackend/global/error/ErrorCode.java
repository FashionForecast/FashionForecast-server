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
	INVALID_WEATHER_END_DATE_TIME(HttpStatus.BAD_REQUEST, "W003", "외출 끝 시간은 현재 시간 또는 외출 시작 시간 보다 과거일 수 없습니다."),
	MIN_MAX_TEMP_NOT_FOUND(HttpStatus.BAD_REQUEST, "W004", "최소/최대 기온을 가져오지 못했습니다."),
	INVALID_TEMP_CONDITION(HttpStatus.BAD_REQUEST, "W005", "올바른 온도 옵션이 아닙니다."),
	TEMP_LEVEL_NOT_FOUND(HttpStatus.BAD_REQUEST, "W006", "기온에 맞는 온도 단계를 찾을 수 없습니다."),

	/**
	 * 옷
	 */
	UMBRELLA_NOT_FOUND(HttpStatus.BAD_REQUEST, "O001", "맞는 우산 타입을 찾을 수 없습니다."),
	LAYERED_NOT_FOUND(HttpStatus.BAD_REQUEST, "O002", "맞는 레이어드 타입을 찾을 수 없습니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}
