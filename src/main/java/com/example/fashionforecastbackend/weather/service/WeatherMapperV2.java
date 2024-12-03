package com.example.fashionforecastbackend.weather.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.example.fashionforecastbackend.region.domain.Region;
import com.example.fashionforecastbackend.weather.domain.RainType;
import com.example.fashionforecastbackend.weather.domain.Season;
import com.example.fashionforecastbackend.weather.domain.SkyStatus;
import com.example.fashionforecastbackend.weather.domain.Weather;
import com.example.fashionforecastbackend.weather.dto.response.WeatherApiV2;

public class WeatherMapperV2 {

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmm");

	public static List<Weather> mapToWeatherForecast(final WeatherApiV2 weatherApiV2, final Region region) {
		return weatherApiV2.forecast().forecastDay().stream()
			.flatMap(day -> day.hour().stream())
			.map(hour -> createWeather(hour, weatherApiV2.current(), region))
			.toList();
	}

	private static Weather createWeather(final WeatherApiV2.Hour hour, final WeatherApiV2.Current current, final Region region) {
		final LocalDateTime lastUpdatedDateTime = current.lastUpdated();
		final String baseDate = lastUpdatedDateTime.format(DATE_FORMATTER);
		final String baseTime = lastUpdatedDateTime.withMinute(0).format(TIME_FORMATTER);
		final LocalDateTime fcstDateTime = hour.time();
		final String fcstDate = fcstDateTime.format(DATE_FORMATTER);
		final String fcstTime = fcstDateTime.withMinute(0).format(TIME_FORMATTER);
		return Weather.builder()
			.nx(region.getNx())
			.ny(region.getNy())
			.baseDate(baseDate)
			.baseTime(baseTime)
			.fcstDate(fcstDate)
			.fcstTime(fcstTime)
			.tmp(String.valueOf(hour.tempC()))
			.reh(String.valueOf(hour.humidity()))
			.wsd(String.valueOf(hour.windKph()))
			.pop(String.valueOf(hour.chanceOfRain()))
			.pcp(String.valueOf(hour.precipMm()))
			.rainType(mapToRainType(hour.condition().code()))
			.skyStatus(mapToSkyStatus(hour.condition().code()))
			.season(Season.fromMonth(fcstDateTime.getMonthValue()))
			.build();
	}


	private static RainType mapToRainType(int code) {
		if (code == 1069) return RainType.RAIN_AND_SNOW;               // 비와 눈
		if (code >= 1063 && code <= 1068) return RainType.RAIN;        // 비
		if (code >= 1114 && code <= 1117) return RainType.SNOW;        // 눈
		if (code >= 1150 && code <= 1153) return RainType.RAIN_DROP;   // 빗방울
		if (code >= 1210 && code <= 1237) return RainType.SNOW;        // 눈
		if (code >= 1273 && code <= 1282) return RainType.RAIN;        // 뇌우를 동반한 비
		return RainType.NONE;
	}

	private static SkyStatus mapToSkyStatus(int code) {
		if (code == 1000) return SkyStatus.CLEAR;              // 맑음
		if (code >= 1003 && code <= 1006) return SkyStatus.PARTLY_CLOUDY;  // 구름많음
		if (code >= 1009) return SkyStatus.CLOUDY;             // 흐림
		return SkyStatus.CLEAR;
	}
}
