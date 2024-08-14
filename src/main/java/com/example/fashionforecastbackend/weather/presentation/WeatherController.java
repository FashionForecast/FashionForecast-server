package com.example.fashionforecastbackend.weather.presentation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.fashionforecastbackend.global.response.ApiResponse;
import com.example.fashionforecastbackend.weather.dto.WeatherRequestDto;
import com.example.fashionforecastbackend.weather.dto.WeatherResponseDto;
import com.example.fashionforecastbackend.weather.service.WeatherService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/weather")
@RequiredArgsConstructor
public class WeatherController {

	private final WeatherService weatherService;

	@GetMapping("/forecast")
	public ApiResponse<List<WeatherResponseDto>> getWeather(
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
		@RequestParam(name = "now") LocalDateTime now,
		@RequestParam(name = "nx") int nx,
		@RequestParam(name = "ny") int ny) {
		WeatherRequestDto dto = new WeatherRequestDto(now, nx, ny);
		return ApiResponse.ok(weatherService.getWeather(dto));
	}

}
