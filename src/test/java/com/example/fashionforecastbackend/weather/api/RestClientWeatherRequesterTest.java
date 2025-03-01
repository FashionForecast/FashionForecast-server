package com.example.fashionforecastbackend.weather.api;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.example.fashionforecastbackend.weather.dto.request.WeatherFilter;

@ExtendWith(MockitoExtension.class)
class RestClientWeatherRequesterTest {
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private RestClient restClient;

	@Mock
	private WeatherApiProperties apiProperties;

	@InjectMocks
	private RestClientWeatherRequester restClientWeatherRequester;

	@Test
	@DisplayName("API 호출이 성공할 경우 응답으로 ok를 반환한다.")
	void getWeatherForecast_API_호출성공() {
		// given
		String mockBaseUrl = "http://mockBaseUrl/";
		String mockApiKey = "mockApiKey";
		given(apiProperties.getBaseUrl()).willReturn(mockBaseUrl);
		given(apiProperties.getKey()).willReturn(mockApiKey);

		String forecastType = "/getVilageFcst";
		String baseDate = "20240809";
		String baseTime = "0600";
		String startDate = "20240809";
		String startTime = "0800";
		String endDate = "20240809";
		String endTime = "1300";
		int nx = 60;
		int ny = 127;

		WeatherFilter weatherFilter = WeatherFilter.of(baseDate, baseTime, startDate, startTime, endDate, endTime, nx,
			ny);

		String path = "/getVilageFcst?serviceKey=";
		given(restClient.get()
			.uri(contains(path))
			.retrieve()
			.body(String.class)
		).willReturn("ok");

		// when
		String response = restClientWeatherRequester.getWeatherForecast(forecastType, weatherFilter);

		// then
		assertThat(response).isEqualTo("ok");
	}

	@Test
	@DisplayName("API 호출이 잘못된 요청값으로 실패할 경우 400상태를 반환한다.")
	void getWeatherForecast_API_호출실패() {
		// given
		String mockBaseUrl = "http://mockBaseUrl/";
		String mockApiKey = "mockApiKey";
		given(apiProperties.getBaseUrl()).willReturn(mockBaseUrl);
		given(apiProperties.getKey()).willReturn(mockApiKey);

		String errorForecastType = "/getVi";
		String errorBaseDate = "202409";
		String errorBaseTime = "060";
		String startDate = "20240809";
		String startTime = "0800";
		String endDate = "20240809";
		String endTime = "1300";
		int nx = 60;
		int ny = 127;

		WeatherFilter errorWeatherFilter = WeatherFilter.of(errorBaseDate, errorBaseTime, startDate, startTime, endDate, endTime, nx,
			ny);

		given(restClient.get()
			.uri(anyString())
			.retrieve()
			.body(String.class)
		).willThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST) {
		});

		// when // then
		assertThatThrownBy(
			() -> restClientWeatherRequester.getWeatherForecast(errorForecastType, errorWeatherFilter))
			.isInstanceOf(RuntimeException.class)
			.hasMessageContaining("400 BAD_REQUEST");
	}
}
