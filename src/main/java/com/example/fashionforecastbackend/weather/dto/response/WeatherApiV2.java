package com.example.fashionforecastbackend.weather.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public record WeatherApiV2(
	@JsonProperty("location") Location location,
	@JsonProperty("current") Current current,
	@JsonProperty("forecast") Forecast forecast
) {
	public record Location(
		@JsonProperty("name") String name
	) {}

	public record Current(
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
		@JsonProperty("last_updated") LocalDateTime lastUpdated
	) {}

	public record Forecast(
		@JsonProperty("forecastday") List<ForecastDay> forecastDay
	) {}

	public record ForecastDay(
		@JsonProperty("hour") List<Hour> hour
	) {}

	public record Hour(
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
		@JsonProperty("time") LocalDateTime time,
		@JsonProperty("temp_c") int tempC,
		@JsonProperty("condition") Condition condition,
		@JsonProperty("wind_kph") double windKph,
		@JsonProperty("humidity") int humidity,
		@JsonProperty("precip_mm") double precipMm,
		@JsonProperty("chance_of_rain") int chanceOfRain
	) {}

	public record Condition(
		@JsonProperty("text") String text,
		@JsonProperty("code") int code
	) {}
}
