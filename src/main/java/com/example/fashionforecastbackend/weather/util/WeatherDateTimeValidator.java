package com.example.fashionforecastbackend.weather.util;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.example.fashionforecastbackend.global.error.ErrorCode;
import com.example.fashionforecastbackend.global.error.exception.InvalidWeatherRequestException;

@Component
public class WeatherDateTimeValidator {

	private static final LocalDateTime now = LocalDateTime.now();

	public void validateNowDateTime(LocalDateTime nowDateTime) {

		if (nowDateTime.isBefore(now.minusMinutes(5))) {
			throw new InvalidWeatherRequestException(ErrorCode.INVALID_WEATHER_NOW_DATE_TIME);
		}

	}

	public void validateStartDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {

		if (startDateTime.isBefore(now) || startDateTime.isAfter(endDateTime)) {
			throw new InvalidWeatherRequestException(ErrorCode.INVALID_WEATHER_START_DATE_TIME);
		}
	}

	public void validateEndDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {

		if (endDateTime.isBefore(now) || endDateTime.isBefore(startDateTime)) {
			throw new InvalidWeatherRequestException(ErrorCode.INVALID_WEATHER_END_DATE_TIME);
		}
	}

}
