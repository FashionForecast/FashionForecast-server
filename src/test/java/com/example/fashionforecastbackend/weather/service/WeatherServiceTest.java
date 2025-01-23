package com.example.fashionforecastbackend.weather.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.fashionforecastbackend.global.error.ErrorCode;
import com.example.fashionforecastbackend.global.error.exception.InvalidWeatherRequestException;
import com.example.fashionforecastbackend.region.domain.Address;
import com.example.fashionforecastbackend.region.domain.Region;
import com.example.fashionforecastbackend.region.domain.repository.RegionRepository;
import com.example.fashionforecastbackend.weather.domain.Weather;
import com.example.fashionforecastbackend.weather.domain.repository.WeatherRepository;
import com.example.fashionforecastbackend.weather.dto.request.WeatherFilter;
import com.example.fashionforecastbackend.weather.dto.request.WeatherRequest;
import com.example.fashionforecastbackend.weather.dto.response.WeatherForecast;
import com.example.fashionforecastbackend.weather.fixture.WeatherFixture;
import com.example.fashionforecastbackend.weather.service.Impl.WeatherServiceImpl;
import com.example.fashionforecastbackend.weather.util.WeatherDateTimeValidator;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

	@InjectMocks
	private WeatherServiceImpl weatherService;
	@Mock
	private RegionRepository regionRepository;
	@Mock
	private WeatherMapper weatherMapper;
	@Mock
	private WeatherRepository weatherRepository;

	@Mock
	private WeatherDateTimeValidator validator;

	@DisplayName("현재시간이 2024-08-11T14:20일때, 외출시간 2024-08-11T15:25 ~ 2024-08-11T19:25 날씨 조회를 성공한다.")
	@Test
	void getWeatherTest() throws Exception {
		//given
		LocalDateTime nowDateTime = LocalDateTime.of(2024, 8, 11, 14, 20);
		LocalDateTime startDateTime = LocalDateTime.of(2024, 8, 11, 15, 25);
		LocalDateTime endDateTime = LocalDateTime.of(2024, 8, 11, 19, 25);

		WeatherRequest requestDto = new WeatherRequest(nowDateTime, startDateTime, endDateTime, 60, 127);
		Region region = Region.builder()
			.address(new Address("11100011", "서울특별시", "강남구", "삼성동"))
			.nx(60)
			.ny(127)
			.build();
		WeatherFilter weatherFilter = WeatherFixture.WEATHER_FILTER;
		List<Weather> weathers = WeatherFixture.WEATHERS;
		List<WeatherForecast> forecasts = WeatherFixture.WEATHER_FORECASTS;

		given(regionRepository.findByNxAndNy(60, 127)).willReturn(Optional.of(region));
		given(weatherRepository.findWeather(weatherFilter)).willReturn(weathers);
		given(weatherMapper.convertToWeatherResponseDto(weathers)).willReturn(forecasts);
		//when
		List<WeatherForecast> result = weatherService.getWeather(requestDto).forecasts();

		//then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(WeatherFixture.WEATHER_FORECASTS.size());
		assertThat(result).isEqualTo(forecasts);

		verify(regionRepository).findByNxAndNy(60, 127);
		verify(weatherRepository).findWeather(weatherFilter);
		verify(weatherMapper).convertToWeatherResponseDto(weathers);

	}

	@DisplayName("날씨 데이터를 변환하는 과정중에 에러가 발생한다.")
	@Test
	void getWeatherErrorTest1() throws Exception {
		// given
		LocalDateTime nowDateTime = LocalDateTime.of(2024, 8, 11, 14, 20);
		LocalDateTime startDateTime = LocalDateTime.of(2024, 8, 11, 15, 25);
		LocalDateTime endDateTime = LocalDateTime.of(2024, 8, 11, 19, 25);

		WeatherRequest requestDto = new WeatherRequest(nowDateTime, startDateTime, endDateTime,60, 127);
		Region region = Region.builder()
			.address(new Address("11100011", "서울특별시", "강남구", "삼성동"))
			.nx(60)
			.ny(127)
			.build();
		List<Weather> weathers = WeatherFixture.WEATHERS;
		WeatherFilter weatherFilter = WeatherFixture.WEATHER_FILTER;


		given(regionRepository.findByNxAndNy(60, 127)).willReturn(Optional.of(region));
		given(weatherRepository.findWeather(weatherFilter)).willReturn(weathers);
		given(weatherMapper.convertToWeatherResponseDto(weathers)).willThrow(
			new RuntimeException("날씨 데이터를 변환하는 과정중에 에러가 발생"));

		// when & then
		assertThatThrownBy(() -> weatherService.getWeather(requestDto))
			.isInstanceOf(RuntimeException.class)
			.hasMessageContaining("날씨 데이터를 변환하는 과정중에 에러가 발생");

		verify(regionRepository).findByNxAndNy(60, 127);
		verify(weatherRepository).findWeather(weatherFilter);

	}

	@DisplayName("날씨 요청 시간이 5분이상 과거이므로 에러가 발생한다.")
	@Test
	void getWeatherErrorTest2() throws Exception {
		//given
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime invalidDateTime = now.minusMinutes(6);
		LocalDateTime startDateTime = now.plusHours(1);
		LocalDateTime endDateTime = startDateTime.plusHours(4);
		int nx = 60;
		int ny = 127;
		WeatherRequest requestDto = new WeatherRequest(invalidDateTime,startDateTime, endDateTime, nx, ny);

		willThrow(new InvalidWeatherRequestException(ErrorCode.INVALID_WEATHER_NOW_DATE_TIME))
			.given(validator).validateNowDateTime(invalidDateTime);

		// when & then
		assertThatThrownBy(() -> weatherService.getWeather(requestDto))
			.isInstanceOf(InvalidWeatherRequestException.class)
			.hasMessageContaining("날씨 조회를 위한 현재 시간은 과거일 수 없습니다.")
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_WEATHER_NOW_DATE_TIME);

	}

}
