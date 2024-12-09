package com.example.fashionforecastbackend.weather.dto.request;

public record WeatherFilter(
	String baseDate,
	String baseTime,
	String startDate,
	String startTime,
	String endDate,
	String endTime,
	double nx,
	double ny
) {

	public static WeatherFilter of(String baseDate, String baseTime, String startDate, String startTime, String endDate,
		String endTime, double nx, double ny) {
		return new WeatherFilter(baseDate, baseTime, startDate, startTime, endDate, endTime, nx, ny);
	}

}
