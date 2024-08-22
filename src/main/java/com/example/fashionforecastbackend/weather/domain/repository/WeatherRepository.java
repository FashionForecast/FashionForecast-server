package com.example.fashionforecastbackend.weather.domain.repository;

import com.example.fashionforecastbackend.weather.domain.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long>, WeatherCustomRepository {

    @Query("SELECT w FROM Weather w WHERE w.baseDate = :baseDate AND w.baseTime = :baseTime AND w.nx = :nx AND w.ny = :ny ORDER BY w.fcstDate ASC, w.fcstTime ASC")
    List<Weather> findWeather(String baseDate, String baseTime, int nx, int ny);

    @Modifying
    @Query("DELETE FROM Weather w WHERE w.baseDate <= :baseDate AND w.baseTime < :baseTime")
    void deletePastWeathers(String baseDate, String baseTime);

}
