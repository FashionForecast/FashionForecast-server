package com.example.fashionforecastbackend.weather.dto.response;

import java.util.List;

import com.example.fashionforecastbackend.weather.domain.Season;

public record WeatherResponse(
	Season season,
	int extremumTmp,
	int maxMinTmpDiff,
	int maximumPop,
	double maximumPcp,
	List<WeatherForecast> forecasts

) {
	public static WeatherResponse of(Season season, int extremumTmp, int maxMinTmpDiff, int maximumPop,
		double maximumPcp, List<WeatherForecast> forecasts) {
		return new WeatherResponse(season, extremumTmp, maxMinTmpDiff, maximumPop, maximumPcp, forecasts);
	}
}
