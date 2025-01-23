package com.example.fashionforecastbackend.weather.api;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.fashionforecastbackend.weather.dto.response.WeatherApiV2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestClientWeatherRequesterV2 {

	private final RestClient restClient;
	private final WeatherApiPropertiesV2 apiProperties;

	@Retryable(
		value = {RestClientException.class},
		maxAttempts = 5,
		backoff = @Backoff(delay = 1000)
	)
	public WeatherApiV2 getWeatherForecast(double lat, double lon, int days) {
		String url = getUrl(lat, lon, days);

		return restClient.get()
			.uri(url)
			.retrieve()
			.body(WeatherApiV2.class);
	}

	private String getUrl(double lat, double lon, int days) {
		return UriComponentsBuilder.fromHttpUrl(apiProperties.getBaseUrl())
			.path("/forecast.json")
			.queryParam("key", apiProperties.getKey())
			.queryParam("q", String.format("%f,%f", lat, lon))
			.queryParam("days", days)
			.queryParam("aqi", "no")
			.build(false)
			.toUriString();
	}
}
