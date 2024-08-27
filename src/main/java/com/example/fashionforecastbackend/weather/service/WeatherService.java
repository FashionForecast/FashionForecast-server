package com.example.fashionforecastbackend.weather.service;

import com.example.fashionforecastbackend.weather.dto.request.WeatherRequest;
import com.example.fashionforecastbackend.weather.dto.response.WeatherResponse;

public interface WeatherService {
	WeatherResponse getWeather(WeatherRequest dto);

}
