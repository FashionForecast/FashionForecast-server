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
	TEMP_LEVEL_NOT_FOUND(HttpStatus.BAD_REQUEST, "W005", "기온에 맞는 온도 단계를 찾을 수 없습니다."),

	/**
	 * 옷
	 */
	UMBRELLA_NOT_FOUND(HttpStatus.BAD_REQUEST, "O001", "맞는 우산 타입을 찾을 수 없습니다."),
	LAYERED_NOT_FOUND(HttpStatus.BAD_REQUEST, "O002", "맞는 레이어드 타입을 찾을 수 없습니다."),

	/**
	 * 최근 검색어
	 */
	SEARCH_NOT_EXIST(HttpStatus.BAD_REQUEST, "S001", "최근 검색어가 존재하지 않습니다."),
	SEARCH_NOT_EXIST_USER(HttpStatus.BAD_REQUEST, "S002", "유저가 존재하지 않습니다."),

	/**
	 * 온도 옵션
	 */
	INVALID_TEMP_CONDITION(HttpStatus.BAD_REQUEST, "OP001", "올바르지 않은 온도 옵션입니다."),
	INVALID_GROUP8_WARM_OPTION(HttpStatus.BAD_REQUEST, "OP002", "온도 그룹 8은 따뜻하게 옵션을 사용할 수 없습니다."),
	INVALID_GROUP1_COOL_OPTION(HttpStatus.BAD_REQUEST, "OP003", "온도 그룹 1은 시원하게 옵션을 사용할 수 없습니다."),

	/**
	 * JWT
	 */

	EXPIRED_PERIOD_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "J001", "기한이 만료된 RefreshToken입니다."),
	INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "J002", "올바르지 않은 RefreshToken입니다."),
	EXPIRED_PERIOD_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "J003", "기한이 만료된 AccessToken입니다."),
	INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "J004", "올바르지 않은 AccessToken입니다."),
	NOT_FOUND_REFRESH_TOKEN(HttpStatus.NOT_FOUND, "J005", "RefreshToken을 찾을 수 없습니다."),
	NOT_MATCH_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "J006",
		"요청된 RefreshToken과 서버에 저장된 RefreshToken 값이 일치하지 않습니다. 다시 로그인 해주세요"),

	/**
	 * Member
	 */

	NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "M001", "회원이 존재하지 않습니다."),
	SOCIAL_LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "M002", "소셜 로그인에 실패하였습니다."),

	/**
	 * 고객의 소리
	 */
	NOT_FOUND_BOARD(HttpStatus.NOT_FOUND, "B001", "게시글이 존재하지 않습니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}
