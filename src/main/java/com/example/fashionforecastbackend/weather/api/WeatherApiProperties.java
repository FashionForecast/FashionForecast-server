package com.example.fashionforecastbackend.weather.api;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "weather.api")
public class WeatherApiProperties {
	private String key;
	private String baseUrl;
	private String ultraSrtNcst;
	private String ultraSrtFcst;
	private String vilageFcst;
}
