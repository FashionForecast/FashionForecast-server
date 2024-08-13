package com.example.fashionforecastbackend.weather.fixture;

import java.util.ArrayList;
import java.util.List;

import com.example.fashionforecastbackend.weather.domain.RainType;
import com.example.fashionforecastbackend.weather.domain.SkyStatus;
import com.example.fashionforecastbackend.weather.domain.Weather;
import com.example.fashionforecastbackend.weather.dto.WeatherResponseDto;

public class WeatherFixture {

	public static final List<WeatherResponseDto> WEATHER_RESPONSE_DTOS = generateWeatherResponseDtos("20240811", "1400",
		"20240812");
	public static final List<Weather> WEATHERS = List.of(
		createWeather("20240811", "1400", "20240811", "1500", 120, 67),
		createWeather("20240811", "1400", "20240811", "1600", 120, 67),
		createWeather("20240811", "1400", "20240811", "1700", 120, 67),
		createWeather("20240811", "1400", "20240811", "1800", 120, 67),
		createWeather("20240811", "1400", "20240811", "1900", 120, 67)
	);

	private static List<WeatherResponseDto> generateWeatherResponseDtos(String baseDate, String baseTime,
		String tomorrow) {
		List<WeatherResponseDto> responses = new ArrayList<>();

		/*
		baseDate
		15시 ~ 23시
		 */
		for (int t = 15; t <= 23; t++) {
			String fcstTime = String.format("%02d00", t);
			responses.add(WeatherResponseDto.from(createWeather(baseDate, baseTime, baseDate, fcstTime, 60, 127)));
		}

		/*
		baseDate 다음날
		0시 ~ 23시
		 */
		for (int t = 0; t <= 23; t++) {
			String fcstTime = String.format("%02d00", t);
			responses.add(WeatherResponseDto.from(createWeather(baseDate, baseTime, tomorrow, fcstTime, 60, 127)));
		}

		return responses;
	}

	public static Weather createWeather(
		String baseDate,
		String baseTime,
		String fcstDate,
		String fcstTime,
		int nx,
		int ny
	) {
		return Weather.builder()
			.nx(nx)
			.ny(ny)
			.baseDate(baseDate)
			.baseTime(baseTime)
			.reh("10")
			.tmp("36")
			.rainType(RainType.NONE)
			.skyStatus(SkyStatus.CLEAR)
			.wsd("1")
			.fcstDate(fcstDate)
			.fcstTime(fcstTime)
			.build();
	}
}