package com.example.fashionforecastbackend.weather.api;

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

	public String getWeatherForecast(String forecastType, WeatherFilter weatherFilter) {

		String url = getUrl(forecastType, weatherFilter);
		log.info(url);
		try {
			return restClient.get()
				.uri(url)
				.retrieve()
				.body(String.class);
		} catch (RestClientException e) {
			log.error("기상청 API를 통해서 날씨 정보를 가져오는데 실패하였습니다.", e);
			throw new RuntimeException(e);
		}
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
			.queryParam("numOfRows", "1000")
			.queryParam("dataType", "JSON")
			.queryParam("base_date", baseDate)
			.queryParam("base_time", baseTime)
			.queryParam("nx", nx)
			.queryParam("ny", ny)
			.build(false)
			.toUriString();
	}

}
