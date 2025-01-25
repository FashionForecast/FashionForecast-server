package com.example.fashionforecastbackend.weather.dto.response;

import java.util.List;

import com.example.fashionforecastbackend.weather.domain.Season;

public record WeatherGroupResponse(
	Season season,
	int extremumTmp,
	int maxMinTmpDiff,
	int maximumPop,
	double maximumPcp,
	List<WeatherGroupForecast> forecasts
) {
}
