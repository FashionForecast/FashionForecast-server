package com.example.fashionforecastbackend.weather.util;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.example.fashionforecastbackend.global.error.ErrorCode;
import com.example.fashionforecastbackend.global.error.exception.InvalidWeatherRequestException;

@Component
public class WeatherDateTimeValidator {

	public void validateNowDateTime(LocalDateTime nowDateTime) {
		if (nowDateTime.isBefore(LocalDateTime.now().minusMinutes(5))) {
			throw new InvalidWeatherRequestException(ErrorCode.INVALID_WEATHER_NOW_DATE_TIME);
		}
	}

	public void validateStartDateTime(LocalDateTime nowDateTime, LocalDateTime startDateTime,
		LocalDateTime endDateTime) {
		nowDateTime = nowDateTime.withMinute(0).withSecond(0).withNano(0);
		startDateTime = startDateTime.withMinute(0).withSecond(0).withNano(0);
		endDateTime = endDateTime.withMinute(0).withSecond(0).withNano(0);
		if (!(startDateTime.isAfter(nowDateTime) || startDateTime.isEqual(nowDateTime))) {
			throw new InvalidWeatherRequestException(ErrorCode.INVALID_WEATHER_START_DATE_TIME);
		}
		if (!(startDateTime.isBefore(endDateTime) || startDateTime.isEqual(endDateTime))) {
			throw new InvalidWeatherRequestException(ErrorCode.INVALID_WEATHER_START_DATE_TIME);
		}
	}

	public void validateEndDateTime(LocalDateTime nowDateTime, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		nowDateTime = nowDateTime.withMinute(0).withSecond(0).withNano(0);
		startDateTime = startDateTime.withMinute(0).withSecond(0).withNano(0);
		endDateTime = endDateTime.withMinute(0).withSecond(0).withNano(0);
		if (!(endDateTime.isAfter(nowDateTime) || endDateTime.isEqual(nowDateTime))) {
			throw new InvalidWeatherRequestException(ErrorCode.INVALID_WEATHER_END_DATE_TIME);
		}
		if (!(endDateTime.isAfter(startDateTime) || endDateTime.isEqual(startDateTime))) {
			throw new InvalidWeatherRequestException(ErrorCode.INVALID_WEATHER_END_DATE_TIME);
		}
	}
}
