package com.example.fashionforecastbackend.weather.service;

import java.util.List;

import com.example.fashionforecastbackend.recommend.dto.RecommendRequest;
import com.example.fashionforecastbackend.weather.dto.WeatherRequestDto;
import com.example.fashionforecastbackend.weather.dto.WeatherResponseDto;
import com.example.fashionforecastbackend.weather.dto.WeatherSummaryResponse;

public interface WeatherService {
	List<WeatherResponseDto> getWeather(WeatherRequestDto dto);

	WeatherSummaryResponse getWeatherByTime(RecommendRequest recommendRequest);
}
