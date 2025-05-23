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

	public static WeatherForecast from(final WeatherGroupForecast weatherGroupForecast) {
		return new WeatherForecast(
			weatherGroupForecast.fcstDate(),
			weatherGroupForecast.fcstTime(),
			weatherGroupForecast.tmp(),
			weatherGroupForecast.reh(),
			weatherGroupForecast.wsd(),
			weatherGroupForecast.pop(),
			weatherGroupForecast.pcp(),
			weatherGroupForecast.rainType(),
			weatherGroupForecast.skyStatus(),
			0.0,
			0.0
		);
	}
}
