package com.example.fashionforecastbackend.weather.api;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.fashionforecastbackend.weather.dto.request.WeatherFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class RestClientWeatherRequester {

	private final RestClient restClient;
	private final WeatherApiProperties apiProperties;

	@Retryable(
		value = {RestClientException.class},
		maxAttempts = 5,
		backoff = @Backoff(delay = 1000)
	)
	public String getWeatherForecast(String forecastType, WeatherFilter weatherFilter) {

		String url = getUrl(forecastType, weatherFilter);
		log.info(url);

		String response = restClient.get()
			.uri(url)
			.retrieve()
			.body(String.class);
		if (response.contains("SERVICE ERROR")) {
			throw new RestClientException("기상청 API 응답에서 'SERVICE ERROR' 발생");
		}
		return response;
	}

	private String getUrl(String forecastType, WeatherFilter weatherFilter) {
		String baseDate = weatherFilter.baseDate();
		String baseTime = weatherFilter.baseTime();
		int nx = weatherFilter.nx();
		int ny = weatherFilter.ny();
		return UriComponentsBuilder.fromHttpUrl(apiProperties.getBaseUrl())
			.path(forecastType)
			.queryParam("serviceKey", apiProperties.getKey())
			.queryParam("pageNo", "1")
			.queryParam("numOfRows", "600")
			.queryParam("dataType", "JSON")
			.queryParam("base_date", baseDate)
			.queryParam("base_time", baseTime)
			.queryParam("nx", nx)
			.queryParam("ny", ny)
			.build(false)
			.toUriString();
	}

}
