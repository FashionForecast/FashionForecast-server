package com.example.fashionforecastbackend.weather.dto;

import java.time.LocalDateTime;

public record WeatherRequestDto(
	LocalDateTime now,
	int nx,
	int ny
) {
}
