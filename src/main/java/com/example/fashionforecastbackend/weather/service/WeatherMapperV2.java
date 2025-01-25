package com.example.fashionforecastbackend.weather.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.example.fashionforecastbackend.region.domain.Region;
import com.example.fashionforecastbackend.weather.domain.RainTypeV2;
import com.example.fashionforecastbackend.weather.domain.Season;
import com.example.fashionforecastbackend.weather.domain.SkyStatusV2;
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
			.rainType(RainTypeV2.getRainTypeByCode(hour.condition().code()))
			.skyStatus(SkyStatusV2.getSkyStatusByCode(hour.condition().code()))
			.season(Season.fromMonth(fcstDateTime.getMonthValue()))
			.build();
	}
}
