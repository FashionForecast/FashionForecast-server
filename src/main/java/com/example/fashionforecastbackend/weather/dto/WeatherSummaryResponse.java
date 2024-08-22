package com.example.fashionforecastbackend.weather.dto;

public record WeatherSummaryResponse(
	int min,
	int max,
	boolean isHighPrecipitationProb,
	boolean isHeavyRainfall
) {
}
