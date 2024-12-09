package com.example.fashionforecastbackend.weather.dto.response;

import com.example.fashionforecastbackend.weather.domain.RainType;
import com.example.fashionforecastbackend.weather.domain.SkyStatus;
import com.example.fashionforecastbackend.weather.domain.Weather;

public record WeatherForecast(
	String fcstDate,
	String fcstTime,
	String tmp,
	String reh,
	String wsd,
	String pop,
	String pcp,
	RainType rainType,
	SkyStatus skyStatus,
	double nx,
	double ny
) {
	public static WeatherForecast from(Weather weather) {
		return new WeatherForecast(
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
