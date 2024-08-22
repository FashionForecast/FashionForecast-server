package com.example.fashionforecastbackend.weather.service.Impl;

import static org.springframework.transaction.annotation.Propagation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fashionforecastbackend.region.domain.Region;
import com.example.fashionforecastbackend.region.domain.repository.RegionRepository;
import com.example.fashionforecastbackend.weather.api.RestClientWeatherRequester;
import com.example.fashionforecastbackend.weather.domain.Weather;
import com.example.fashionforecastbackend.weather.domain.repository.WeatherRepository;
import com.example.fashionforecastbackend.weather.dto.WeatherApiResponse;
import com.example.fashionforecastbackend.weather.dto.WeatherRequestDto;
import com.example.fashionforecastbackend.weather.dto.WeatherResponseDto;
import com.example.fashionforecastbackend.weather.service.WeatherMapper;
import com.example.fashionforecastbackend.weather.service.WeatherService;
import com.example.fashionforecastbackend.weather.util.WeatherValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherServiceImpl implements WeatherService {

	private final RestClientWeatherRequester weatherRequester;
	private final RegionRepository regionRepository;
	private final WeatherMapper weatherMapper;
	private final WeatherRepository weatherRepository;
	private final WeatherValidator validator;

	@Transactional
	@Override
	public List<WeatherResponseDto> getWeather(final WeatherRequestDto dto) {
		LocalDateTime now = dto.now();
		validator.validateDateTime(now);
		String baseDate = getBaseDate(now);
		String baseTime = getBaseTime(now);

		Region region = findRegion(dto.nx(), dto.ny());

		Collection<Weather> weathers = weatherRepository.findWeather(baseDate, baseTime, region.getNx(),
			region.getNy());

		if (!weathers.isEmpty()) {
			return weatherMapper.convertToWeatherResponseDto(weathers);
		}

		try {
			int nx = region.getNx();
			int ny = region.getNy();
			String weatherForecast = weatherRequester.getWeatherForecast("/getVilageFcst", baseDate, baseTime, nx,
				ny);

			List<WeatherApiResponse> responses = weatherMapper.convertToWeatherApiResponse(weatherForecast,
				"/response/body/items/item");

			weathers = weatherMapper.convertToWeathers(responses, region, "short");
			weatherRepository.saveAll(weathers);
			List<WeatherResponseDto> weatherResponseDtos = weatherMapper.convertToWeatherResponseDto(weathers);

			weatherResponseDtos.sort((r1, r2) -> {
				String dateTime1 = r1.fcstDate() + r1.fcstTime();
				String dateTime2 = r2.fcstDate() + r2.fcstTime();
				return dateTime1.compareTo(dateTime2);
			});
			return weatherResponseDtos;

		} catch (Exception e) {
			log.error("날씨 저장하는 과정에서 문제가 발생했습니다.", e);
			throw new RuntimeException("날씨를 조회하지 못하였습니다.");
		}
	}

	@Async
	@Transactional(propagation = REQUIRES_NEW)
	@Scheduled(cron = "0 0 5 * * ?")
	public void deletePastWeathers() {
		LocalDateTime now = LocalDateTime.now();
		String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String baseTime = getBaseTime(now);
		weatherRepository.deletePastWeathers(baseDate, baseTime);
	}

	private String getBaseDate(LocalDateTime dateTime) {
		int hour = dateTime.getHour();
		if (hour < 2) {
			dateTime = dateTime.minusDays(1);
		}
		return dateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	}

	private String getBaseTime(LocalDateTime dateTime) {

		int hour = dateTime.getHour();
		// 예보는 3시간 기점으로 발표
		if (hour < 2) {
			hour = 23;
		} else if (hour < 5) {
			hour = 2;
		} else if (hour < 8) {
			hour = 5;
		} else if (hour < 11) {
			hour = 8;
		} else if (hour < 14) {
			hour = 11;
		} else if (hour < 17) {
			hour = 14;
		} else if (hour < 20) {
			hour = 17;
		} else if (hour < 23) {
			hour = 20;
		} else {
			hour = 23;
		}

		StringBuilder sb = new StringBuilder();
		if (hour < 10) {
			sb.append("0").append(hour);
		} else {
			sb.append(hour);
		}

		sb.append("00");

		return sb.toString();
	}

	private Region findRegion(int nx, int ny) {

		Region region = regionRepository.findByNxAndNy(nx, ny).orElse(null);
		if (Objects.equals(region, null)) {
			List<Region> regions = regionRepository.findAll();
			Collections.sort(regions, (r1, r2) -> {
				int diff1 = Math.abs(r1.getNx() - nx) + Math.abs(r1.getNy() - ny);
				int diff2 = Math.abs(r2.getNx() - nx) + Math.abs(r2.getNy() - ny);
				return diff1 - diff2;
			});
			region = regions.get(0);
		}
		return region;
	}

}
