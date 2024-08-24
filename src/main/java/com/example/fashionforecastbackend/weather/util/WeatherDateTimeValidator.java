package com.example.fashionforecastbackend.weather.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.example.fashionforecastbackend.global.error.ErrorCode;
import com.example.fashionforecastbackend.global.error.exception.InvalidWeatherRequestException;

@Component
public class WeatherDateTimeValidator {

	private static final LocalDateTime now = LocalDateTime.now();
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
	private static final String NOW_DATE = now.format(DATE_FORMAT);

	public void validateNowDateTime(LocalDateTime nowDateTime) {

		if (nowDateTime.isBefore(now.minusMinutes(5))) {
			throw new InvalidWeatherRequestException(ErrorCode.INVALID_WEATHER_NOW_DATE_TIME);
		}

	}

	public void validateStartDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {

		String startDate = startDateTime.format(DATE_FORMAT);
		String endDate = endDateTime.format(DATE_FORMAT);

		int nowHour = now.getHour();
		int startHour = startDateTime.getHour();
		int endHour = endDateTime.getHour();

		if (startDate.compareTo(NOW_DATE) < 0 || startDate.compareTo(endDate) > 0) {
			throw new InvalidWeatherRequestException(ErrorCode.INVALID_WEATHER_START_DATE_TIME);
		}

		if (startHour < nowHour || startHour > endHour) {
			throw new InvalidWeatherRequestException(ErrorCode.INVALID_WEATHER_START_DATE_TIME);
		}
	}

	public void validateEndDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {

		String startDate = startDateTime.format(DATE_FORMAT);
		String endDate = endDateTime.format(DATE_FORMAT);

		int nowHour = now.getHour();
		int startHour = startDateTime.getHour();
		int endHour = endDateTime.getHour();

		if (endDate.compareTo(NOW_DATE) < 0 || endDate.compareTo(startDate) < 0) {
			throw new InvalidWeatherRequestException(ErrorCode.INVALID_WEATHER_END_DATE_TIME);
		}

		if (endHour <= nowHour || endHour <= startHour) {
			throw new InvalidWeatherRequestException(ErrorCode.INVALID_WEATHER_END_DATE_TIME);
		}
	}

}
