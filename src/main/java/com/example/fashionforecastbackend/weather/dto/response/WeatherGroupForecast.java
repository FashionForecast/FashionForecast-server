package com.example.fashionforecastbackend.weather.dto.response;

import com.example.fashionforecastbackend.weather.domain.RainType;
import com.example.fashionforecastbackend.weather.domain.SkyStatus;

public record WeatherGroupForecast(
	String fcstDate,
	String fcstTime,
	String tmp,
	String reh,
	String wsd,
	String pop,
	String pcp,
	RainType rainType,
	SkyStatus skyStatus,
	boolean isSelected
) {
	public static WeatherGroupForecast of(final WeatherForecast weatherForecast, final boolean isSelected) {
		return new WeatherGroupForecast(
			weatherForecast.fcstDate(),
			weatherForecast.fcstTime(),
			weatherForecast.tmp(),
			weatherForecast.reh(),
			weatherForecast.wsd(),
			weatherForecast.pop(),
			weatherForecast.pcp(),
			weatherForecast.rainType(),
			weatherForecast.skyStatus(),
			isSelected
		);
	}
}
