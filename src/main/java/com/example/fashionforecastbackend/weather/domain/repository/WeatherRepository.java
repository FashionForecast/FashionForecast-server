package com.example.fashionforecastbackend.weather.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.fashionforecastbackend.weather.domain.Weather;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long>, WeatherCustomRepository, WeatherQueryRepository {

}
