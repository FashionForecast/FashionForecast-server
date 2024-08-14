package com.example.fashionforecastbackend.weather.service.Impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

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

	@Override
	public List<WeatherResponseDto> getWeather(WeatherRequestDto dto) {
		LocalDateTime now = dto.now();
		String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String baseTime = getBaseTime(now);
		Region region = findRegion(dto.nx(), dto.ny());

		Collection<Weather> weathers = weatherRepository.findByBaseDateAndBaseTimeAndNxAndNy(baseDate,
			baseTime, region.getNx(), region.getNy());

		log.info(baseDate);
		log.info(baseTime);
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
			return weatherMapper.convertToWeatherResponseDto(weathers);

		} catch (Exception e) {
			log.error("날씨 저장하는 과정에서 문제가 발생했습니다.", e);
			throw new RuntimeException("날씨를 조회하지 못하였습니다.");
		}
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
