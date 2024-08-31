package com.example.fashionforecastbackend.weather.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.example.fashionforecastbackend.global.error.ErrorCode;
import com.example.fashionforecastbackend.global.error.exception.InvalidWeatherRequestException;

@Component
public class WeatherDateTimeValidator {

	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

	public void validateNowDateTime(LocalDateTime nowDateTime) {

		if (nowDateTime.isBefore(LocalDateTime.now().minusMinutes(5))) {
			throw new InvalidWeatherRequestException(ErrorCode.INVALID_WEATHER_NOW_DATE_TIME);
		}

	}

	public void validateStartDateTime(LocalDateTime nowDateTime, LocalDateTime startDateTime, LocalDateTime endDateTime) {

		String nowDate = nowDateTime.format(DATE_FORMAT);
		String startDate = startDateTime.format(DATE_FORMAT);
		String endDate = endDateTime.format(DATE_FORMAT);

		if (startDate.compareTo(nowDate) > 0) {
			return;
		}

		int nowHour = nowDateTime.getHour();
		int startHour = startDateTime.getHour();
		int endHour = endDateTime.getHour();

		if (startDate.compareTo(nowDate) < 0 || startDate.compareTo(endDate) > 0) {
			throw new InvalidWeatherRequestException(ErrorCode.INVALID_WEATHER_START_DATE_TIME);
		}

		if (startHour < nowHour || startHour > endHour) {
			throw new InvalidWeatherRequestException(ErrorCode.INVALID_WEATHER_START_DATE_TIME);
		}
	}

	public void validateEndDateTime(LocalDateTime nowDateTime, LocalDateTime startDateTime, LocalDateTime endDateTime) {

		String nowDate = nowDateTime.format(DATE_FORMAT);
		String startDate = startDateTime.format(DATE_FORMAT);
		String endDate = endDateTime.format(DATE_FORMAT);

		if (endDate.compareTo(startDate) > 0 || endDate.compareTo(nowDate) > 0) {
			return;
		}

		int nowHour = nowDateTime.getHour();
		int startHour = startDateTime.getHour();
		int endHour = endDateTime.getHour();

		if (endDate.compareTo(nowDate) < 0 || endDate.compareTo(startDate) < 0) {
			throw new InvalidWeatherRequestException(ErrorCode.INVALID_WEATHER_END_DATE_TIME);
		}

		if (endHour < nowHour || endHour < startHour) {
			throw new InvalidWeatherRequestException(ErrorCode.INVALID_WEATHER_END_DATE_TIME);
		}
	}

}
