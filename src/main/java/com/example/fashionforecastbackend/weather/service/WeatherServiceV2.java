package com.example.fashionforecastbackend.weather.service;

import com.example.fashionforecastbackend.weather.dto.request.WeatherTotalGroupRequest;
import com.example.fashionforecastbackend.weather.dto.response.WeatherGroupResponse;

public interface WeatherServiceV2 extends WeatherService{
	WeatherGroupResponse getWeatherGroup(final WeatherTotalGroupRequest request);
}
