package com.example.fashionforecastbackend.weather.api;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "weather-v2.api")
public class WeatherApiPropertiesV2 {
	private String baseUrl;
	private String apiKey;
}
