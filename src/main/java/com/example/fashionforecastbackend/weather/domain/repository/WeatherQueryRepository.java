package com.example.fashionforecastbackend.weather.domain.repository;

import java.util.List;

import com.example.fashionforecastbackend.weather.domain.Weather;
import com.example.fashionforecastbackend.weather.dto.request.WeatherFilter;

public interface WeatherQueryRepository {

	List<Weather> findWeather(WeatherFilter weatherFilter);

	void deletePastWeathers(String nowBaseDateTime);


}
