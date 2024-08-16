package com.example.fashionforecastbackend.weather.dto;

import com.example.fashionforecastbackend.weather.domain.RainType;
import com.example.fashionforecastbackend.weather.domain.SkyStatus;
import com.example.fashionforecastbackend.weather.domain.Weather;

public record WeatherResponseDto(
	String fcstDate,
	String fcstTime,
	String tmp,
	String reh,
	String wsd,
	String pop,
	String pcp,
	RainType rainType,
	SkyStatus skyStatus,
	int nx,
	int ny
) {
	public static WeatherResponseDto from(Weather weather) {
		return new WeatherResponseDto(
			weather.getFcstDate(),
			weather.getFcstTime(),
			weather.getTmp(),
			weather.getReh(),
			weather.getWsd(),
			weather.getPop(),
			weather.getPcp(),
			weather.getRainType(),
			weather.getSkyStatus(),
			weather.getNx(),
			weather.getNy()
		);
	}
}
