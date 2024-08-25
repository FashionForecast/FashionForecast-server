package com.example.fashionforecastbackend.weather.fixture;

import java.util.ArrayList;
import java.util.List;

import com.example.fashionforecastbackend.weather.domain.RainType;
import com.example.fashionforecastbackend.weather.domain.Season;
import com.example.fashionforecastbackend.weather.domain.SkyStatus;
import com.example.fashionforecastbackend.weather.domain.Weather;
import com.example.fashionforecastbackend.weather.dto.request.WeatherFilter;
import com.example.fashionforecastbackend.weather.dto.response.WeatherForecast;
import com.example.fashionforecastbackend.weather.dto.response.WeatherResponse;

public class WeatherFixture {

	public static final List<WeatherForecast> WEATHER_FORECASTS = generateWeatherResponseDtos("20240811", "1400");
	public static final WeatherResponse WEATHER_RESPONSE = WeatherResponse.of(Season.SUMMER, 36, 0, 30,
		0.0, WEATHER_FORECASTS);
	public static final List<Weather> WEATHERS = List.of(
		createWeather("20240811", "1400", "20240811", "1500", 120, 67),
		createWeather("20240811", "1400", "20240811", "1600", 120, 67),
		createWeather("20240811", "1400", "20240811", "1700", 120, 67),
		createWeather("20240811", "1400", "20240811", "1800", 120, 67),
		createWeather("20240811", "1400", "20240811", "1900", 120, 67)
	);

	public static final WeatherFilter WEATHER_FILTER = WeatherFilter.of("20240811", "1400", "20240811", "1500",
		"20240811", "1900", 60, 127);

	private static List<WeatherForecast> generateWeatherResponseDtos(String baseDate, String baseTime) {
		List<WeatherForecast> responses = new ArrayList<>();

		/*
		외출 시간
		16시 ~ 23시
		 */
		for (int t = 16; t <= 21; t++) {
			String fcstTime = String.format("%02d00", t);
			responses.add(WeatherForecast.from(createWeather(baseDate, baseTime, baseDate, fcstTime, 60, 127)));
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
			.pcp("0.0mm")
			.pop("30")
			.season(Season.SUMMER)
			.rainType(RainType.NONE)
			.skyStatus(SkyStatus.CLEAR)
			.wsd("1")
			.fcstDate(fcstDate)
			.fcstTime(fcstTime)
			.build();
	}
}