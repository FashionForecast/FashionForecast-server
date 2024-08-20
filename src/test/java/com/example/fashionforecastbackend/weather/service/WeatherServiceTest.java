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
import com.example.fashionforecastbackend.weather.dto.WeatherRequestDto;
import com.example.fashionforecastbackend.weather.dto.WeatherResponseDto;
import com.example.fashionforecastbackend.weather.fixture.WeatherFixture;
import com.example.fashionforecastbackend.weather.service.Impl.WeatherServiceImpl;
import com.example.fashionforecastbackend.weather.util.WeatherValidator;

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
	private WeatherValidator validator;

	@DisplayName("2024년 8월 11일 14시 20분에 날씨 조회를 성공한다.")
	@Test
	void getWeatherTest() throws Exception {
		//given
		LocalDateTime dateTime = LocalDateTime.of(2024, 8, 11, 14, 20);
		WeatherRequestDto requestDto = new WeatherRequestDto(dateTime, 60, 127);
		Region region = Region.builder()
			.address(new Address("11100011", "서울특별시", "강남구", "삼성동"))
			.nx(60)
			.ny(127)
			.build();
		List<Weather> weathers = WeatherFixture.WEATHERS;
		List<WeatherResponseDto> responseDtos = WeatherFixture.WEATHER_RESPONSE_DTOS;

		willDoNothing().given(validator).validateDateTime(dateTime);
		given(regionRepository.findByNxAndNy(60, 127)).willReturn(Optional.of(region));
		given(weatherRepository.findWeather("20240811", "1400", 60, 127)).willReturn(weathers);
		given(weatherMapper.convertToWeatherResponseDto(weathers)).willReturn(responseDtos);
		//when
		List<WeatherResponseDto> result = weatherService.getWeather(requestDto);

		//then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(WeatherFixture.WEATHER_RESPONSE_DTOS.size());
		assertThat(result).isEqualTo(responseDtos);

		verify(regionRepository).findByNxAndNy(60, 127);
		verify(weatherRepository).findWeather("20240811", "1400", 60, 127);
		verify(weatherMapper).convertToWeatherResponseDto(weathers);

	}

	@DisplayName("날씨 데이터를 변환하는 과정중에 에러가 발생한다.")
	@Test
	void getWeatherErrorTest1() throws Exception {
		// given
		LocalDateTime dateTime = LocalDateTime.of(2024, 8, 11, 14, 20);
		WeatherRequestDto requestDto = new WeatherRequestDto(dateTime, 60, 127);
		Region region = Region.builder()
			.address(new Address("11100011", "서울특별시", "강남구", "삼성동"))
			.nx(60)
			.ny(127)
			.build();
		List<Weather> weathers = WeatherFixture.WEATHERS;

		willDoNothing().given(validator).validateDateTime(dateTime);
		given(regionRepository.findByNxAndNy(60, 127)).willReturn(Optional.of(region));
		given(weatherRepository.findWeather("20240811", "1400", 60, 127)).willReturn(weathers);
		given(weatherMapper.convertToWeatherResponseDto(weathers)).willThrow(
			new RuntimeException("날씨 데이터를 변환하는 과정중에 에러가 발생"));

		// when & then
		assertThatThrownBy(() -> weatherService.getWeather(requestDto))
			.isInstanceOf(RuntimeException.class)
			.hasMessageContaining("날씨 데이터를 변환하는 과정중에 에러가 발생");

		verify(regionRepository).findByNxAndNy(60, 127);
		verify(weatherRepository).findWeather("20240811", "1400", 60, 127);

	}

	@DisplayName("날씨 요청 시간이 5분이상 과거이므로 에러가 발생한다.")
	@Test
	void getWeatherErrorTest2() throws Exception {
		//given
		LocalDateTime invalidDateTime = LocalDateTime.now().minusMinutes(5);
		int nx = 60;
		int ny = 127;
		WeatherRequestDto requestDto = new WeatherRequestDto(invalidDateTime, nx, ny);

		willThrow(new InvalidWeatherRequestException(ErrorCode.INVALID_WEATHER_REQUEST_TIME))
			.given(validator).validateDateTime(invalidDateTime);

		// when & then
		assertThatThrownBy(() -> weatherService.getWeather(requestDto))
			.isInstanceOf(InvalidWeatherRequestException.class)
			.hasMessageContaining("날씨 요청 시간은 과거일 수 없습니다.")
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_WEATHER_REQUEST_TIME);

	}

}