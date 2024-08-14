package com.example.fashionforecastbackend.weather.domain.repository;

import java.util.Collection;

import com.example.fashionforecastbackend.weather.domain.Weather;

public interface WeatherCustomRepository {

	void saveAll(Collection<Weather> weathers);
}
