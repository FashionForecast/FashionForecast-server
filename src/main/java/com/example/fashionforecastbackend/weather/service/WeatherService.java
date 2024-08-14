package com.example.fashionforecastbackend.weather.service;

import java.util.List;

import com.example.fashionforecastbackend.weather.dto.WeatherRequestDto;
import com.example.fashionforecastbackend.weather.dto.WeatherResponseDto;

public interface WeatherService {
	List<WeatherResponseDto> getWeather(WeatherRequestDto dto);
}
