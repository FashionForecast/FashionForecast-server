package com.example.fashionforecastbackend.weather.fixture;

import java.util.ArrayList;
import java.util.List;

import com.example.fashionforecastbackend.weather.domain.RainType;
import com.example.fashionforecastbackend.weather.domain.Season;
import com.example.fashionforecastbackend.weather.domain.SkyStatus;
import com.example.fashionforecastbackend.weather.domain.Weather;
import com.example.fashionforecastbackend.weather.dto.request.WeatherFilter;
import com.example.fashionforecastbackend.weather.dto.response.WeatherForecast;
import com.example.fashionforecastbackend.weather.dto.response.WeatherGroupForecast;
import com.example.fashionforecastbackend.weather.dto.response.WeatherGroupResponse;
import com.example.fashionforecastbackend.weather.dto.response.WeatherResponse;

public class WeatherFixture {

	public static final List<WeatherForecast> WEATHER_FORECASTS = generateWeatherResponseDtos("20240811", "1100");
	public static final WeatherResponse WEATHER_RESPONSE = WeatherResponse.of(Season.SUMMER, 36, 0, 30,
		0.0, WEATHER_FORECASTS);
	public static final List<Weather> WEATHERS = List.of(
		createWeather("20240811", "1400", "20240811", "1500", 34, 126),
		createWeather("20240811", "1400", "20240811", "1600", 34, 126),
		createWeather("20240811", "1400", "20240811", "1700", 34, 126),
		createWeather("20240811", "1400", "20240811", "1800", 34, 126),
		createWeather("20240811", "1400", "20240811", "1900", 34, 126)
	);

	public static final WeatherGroupResponse WEATHER_GROUP_RESPONSE = new WeatherGroupResponse(Season.WINTER, 2, 0, 0, 0.0,
		List.of(
			new WeatherGroupForecast("20250124", "1200", "3", "10", "1", "0", "0.0mm", RainType.NONE, SkyStatus.CLEAR, false),
			new WeatherGroupForecast("20250124", "1300", "4", "10", "3", "0", "0.0mm", RainType.NONE, SkyStatus.CLEAR, false),
			new WeatherGroupForecast("20250124", "1400", "0", "20", "1", "80", "10.0mm", RainType.SNOW, SkyStatus.CLOUDY, false),
			new WeatherGroupForecast("20250124", "1500", "2", "40", "2", "100", "15.0mm", RainType.SNOW, SkyStatus.CLOUDY, true),
			new WeatherGroupForecast("20250124", "1600", "3", "30", "4", "90", "6.0mm", RainType.SNOW, SkyStatus.CLOUDY, true),
			new WeatherGroupForecast("20250124", "1700", "4", "10", "1", "0", "0.0mm", RainType.NONE, SkyStatus.CLEAR, true)
		));

	public static final WeatherFilter WEATHER_FILTER = WeatherFilter.of("20240811", "1100", "20240811", "1500",
		"20240811", "1900", 60, 127);

	private static List<WeatherForecast> generateWeatherResponseDtos(String baseDate, String baseTime) {
		List<WeatherForecast> responses = new ArrayList<>();

		/*
		외출 시간
		16시 ~ 23시
		 */
		for (int t = 16; t <= 21; t++) {
			String fcstTime = String.format("%02d00", t);
			responses.add(WeatherForecast.from(createWeather(baseDate, baseTime, baseDate, fcstTime, 34, 126)));
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
