package com.example.fashionforecastbackend.weather.util;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.example.fashionforecastbackend.global.error.ErrorCode;
import com.example.fashionforecastbackend.global.error.exception.InvalidWeatherRequestException;

@Component
public class WeatherValidator {

	public void validateDateTime(LocalDateTime dateTime) {

		LocalDateTime now = LocalDateTime.now();
		if (dateTime.isBefore(now.minusMinutes(5))) {
			throw new InvalidWeatherRequestException(ErrorCode.INVALID_WEATHER_REQUEST_TIME);
		}

	}
}
