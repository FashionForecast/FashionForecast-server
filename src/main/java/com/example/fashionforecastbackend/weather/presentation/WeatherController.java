package com.example.fashionforecastbackend.weather.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fashionforecastbackend.global.response.ApiResponse;
import com.example.fashionforecastbackend.weather.dto.request.WeatherRequest;
import com.example.fashionforecastbackend.weather.dto.response.WeatherResponse;
import com.example.fashionforecastbackend.weather.service.WeatherService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/weather")
@RequiredArgsConstructor
public class WeatherController {

	private final WeatherService weatherService;

	@GetMapping("/forecast")
	public ApiResponse<WeatherResponse> getWeather(
		@ModelAttribute @Valid WeatherRequest dto) {
		return ApiResponse.ok(weatherService.getWeather(dto));
	}

}
