package com.example.fashionforecastbackend.weather.dto.request;

public record WeatherFilter(
	String baseDate,
	String baseTime,
	String startDate,
	String startTime,
	String endDate,
	String endTime,
	int nx,
	int ny
) {

	public static WeatherFilter of(String baseDate, String baseTime, String startDate, String startTime, String endDate,
		String endTime, int nx, int ny) {
		return new WeatherFilter(baseDate, baseTime, startDate, startTime, endDate, endTime, nx, ny);
	}

}
