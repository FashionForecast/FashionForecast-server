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

		int nowHour = now.getHour();
		int startHour = startDateTime.getHour();
		int endHour = endDateTime.getHour();

		if (startHour < nowHour || startHour > endHour) {
			throw new InvalidWeatherRequestException(ErrorCode.INVALID_WEATHER_START_DATE_TIME);
		}
	}

	public void validateEndDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {

		int nowHour = now.getHour();
		int startHour = startDateTime.getHour();
		int endHour = endDateTime.getHour();

		if (endHour <= nowHour || endHour <= startHour) {
			throw new InvalidWeatherRequestException(ErrorCode.INVALID_WEATHER_END_DATE_TIME);
		}
	}

}
