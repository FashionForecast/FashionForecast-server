package com.example.fashionforecastbackend.weather.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.fashionforecastbackend.weather.domain.Weather;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long>, WeatherCustomRepository {

	@Query("SELECT w FROM Weather w WHERE w.baseDate = :baseDate AND w.baseTime = :baseTime AND w.nx = :nx AND w.ny = :ny ORDER BY w.fcstDate ASC, w.fcstTime ASC")
	List<Weather> findWeather(String baseDate, String baseTime, int nx, int ny);
}
