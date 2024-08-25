package com.example.fashionforecastbackend.weather.service.Impl;

import static org.springframework.transaction.annotation.Propagation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fashionforecastbackend.region.domain.Region;
import com.example.fashionforecastbackend.region.domain.repository.RegionRepository;
import com.example.fashionforecastbackend.weather.api.RestClientWeatherRequester;
import com.example.fashionforecastbackend.weather.domain.Season;
import com.example.fashionforecastbackend.weather.domain.Weather;
import com.example.fashionforecastbackend.weather.domain.repository.WeatherRepository;
import com.example.fashionforecastbackend.weather.dto.request.WeatherFilter;
import com.example.fashionforecastbackend.weather.dto.request.WeatherRequest;
import com.example.fashionforecastbackend.weather.dto.response.WeatherApi;
import com.example.fashionforecastbackend.weather.dto.response.WeatherForecast;
import com.example.fashionforecastbackend.weather.dto.response.WeatherResponse;
import com.example.fashionforecastbackend.weather.service.WeatherMapper;
import com.example.fashionforecastbackend.weather.service.WeatherService;
import com.example.fashionforecastbackend.weather.util.WeatherDateTimeValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherServiceImpl implements WeatherService {

	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

	private final RestClientWeatherRequester weatherRequester;
	private final RegionRepository regionRepository;
	private final WeatherMapper weatherMapper;
	private final WeatherRepository weatherRepository;
	private final WeatherDateTimeValidator validator;

	@Transactional
	@Override
	public WeatherResponse getWeather(final WeatherRequest dto) {
		validateDtoDateTime(dto);
		Region region = findRegion(dto.nx(), dto.ny());
		WeatherFilter weatherFilter = getWeatherFilter(dto, region);

		List<Weather> weathers = weatherRepository.findWeather(weatherFilter);

		if (weathers.isEmpty() || isUnderSize(weathers, weatherFilter)) {
			weathers = getWeathersFromExternalApi(weatherFilter, region);
			weatherRepository.saveAll(weathers);
			weathers = getFilteredWeathers(weathers, weatherFilter);
			sortWeathersByDateTime(weathers);
		}

		Season season = weathers.get(0).getSeason();
		List<WeatherForecast> weatherForecasts = weatherMapper.convertToWeatherResponseDto(weathers);

		return getWeatherResponse(weatherForecasts, season);
	}

	@Async
	@Transactional(propagation = REQUIRES_NEW)
	@Scheduled(cron = "0 0 5 * * ?")
	public void deletePastWeathers() {
		LocalDateTime now = LocalDateTime.now();
		String nowBaseDateTime = now.format(DATE_FORMAT) + getBaseTime(now);
		weatherRepository.deletePastWeathers(nowBaseDateTime);
	}

	private boolean isUnderSize(List<Weather> weathers, WeatherFilter weatherFilter) {
		int startTime = Integer.parseInt(weatherFilter.startTime().substring(2));
		int endTime = Integer.parseInt(weatherFilter.endTime().substring(2));
		int requestSize = (endTime - startTime) + 1;
		return (weathers.size() < requestSize);
	}

	private WeatherResponse getWeatherResponse(List<WeatherForecast> weatherForecasts, Season season) {
		int maximumTmp = getMaximumTmp(weatherForecasts);
		int minimumTmp = getMinimumTmp(weatherForecasts);
		int extremumTmp = getExtremumTmp(maximumTmp, minimumTmp, season);
		int maxMinTmpDiff = maximumTmp - minimumTmp;
		int maximumPop = getMaximumPop(weatherForecasts);
		double maximumPcp = getMaximumPcp(weatherForecasts);
		return WeatherResponse.of(season, extremumTmp, maxMinTmpDiff, maximumPop, maximumPcp, weatherForecasts);
	}

	private int getExtremumTmp(int maximumTmp, int minimumTmp, Season season) {
		int extremumTmp = 0;
		if (Objects.equals(season, Season.SUMMER)) {
			extremumTmp = maximumTmp;
		}
		if (Objects.equals(season, Season.WINTER)) {
			extremumTmp = minimumTmp;
		}
		return extremumTmp;
	}

	private int getMaximumTmp(List<WeatherForecast> weatherForecasts) {
		return weatherForecasts.stream()
			.map(weather -> Integer.parseInt(weather.tmp()))
			.max(Integer::compareTo)
			.orElse(0);
	}

	private int getMinimumTmp(List<WeatherForecast> weatherForecasts) {
		return weatherForecasts.stream()
			.map(weather -> Integer.parseInt(weather.tmp()))
			.min(Integer::compareTo)
			.orElse(0);
	}

	private int getMaximumPop(List<WeatherForecast> weatherForecasts) {
		return weatherForecasts.stream()
			.map(weather -> Integer.parseInt(weather.pop()))
			.max(Integer::compareTo)
			.orElse(0);
	}

	private double getMaximumPcp(List<WeatherForecast> weatherForecasts) {
		return weatherForecasts.stream()
			.map(weather -> weather.pcp().replaceAll("[^\\d.]", ""))
			.mapToDouble(Double::parseDouble)
			.max()
			.orElse(0.0);
	}

	private List<Weather> getWeathersFromExternalApi(WeatherFilter weatherFilter, Region region) {
		try {
			String weatherForecast = weatherRequester.getWeatherForecast("/getVilageFcst", weatherFilter);
			List<WeatherApi> responses = weatherMapper.convertToWeatherApiResponse(weatherForecast,
				"/response/body/items/item");
			return weatherMapper.convertToWeathers(responses, region);

		} catch (Exception e) {
			log.error("외부 API에서 날씨를 가져오는 과정에서 문제가 발생했습니다.", e);
			throw new RuntimeException("날씨를 조회하지 못하였습니다.");
		}
	}

	private void validateDtoDateTime(WeatherRequest dto) {

		LocalDateTime nowDateTime = dto.nowDateTime();
		LocalDateTime startDateTime = dto.startDateTime();
		LocalDateTime endDateTime = dto.endDateTime();

		validator.validateNowDateTime(nowDateTime);
		validator.validateStartDateTime(nowDateTime, startDateTime, endDateTime);
		validator.validateEndDateTime(nowDateTime, startDateTime, endDateTime);

	}

	private void sortWeathersByDateTime(List<Weather> weathers) {
		weathers.sort((r1, r2) -> {
			String dateTime1 = r1.getFcstDate() + r1.getFcstTime();
			String dateTime2 = r2.getFcstDate() + r2.getFcstTime();
			return dateTime1.compareTo(dateTime2);
		});
	}

	private List<Weather> getFilteredWeathers(Collection<Weather> weathers, WeatherFilter weatherFilter) {
		return weathers.stream()
			.filter(weather -> isInDateTime(weather, weatherFilter))
			.collect(Collectors.toList());
	}

	private boolean isInDateTime(Weather weather, WeatherFilter weatherFilter) {

		String startDateTime = weatherFilter.startDate() + weatherFilter.startTime();
		String endDateTime = weatherFilter.endDate() + weatherFilter.endTime();
		String fcstDateTime = weather.getFcstDate() + weather.getFcstTime();

		return fcstDateTime.compareTo(startDateTime) >= 0 &&
			fcstDateTime.compareTo(endDateTime) <= 0;
	}

	private String getBaseDate(LocalDateTime nowDateTime) {
		int hour = nowDateTime.getHour();
		if (hour < 2) {
			nowDateTime = nowDateTime.minusDays(1);
		}
		return nowDateTime.format(DATE_FORMAT);
	}

	private WeatherFilter getWeatherFilter(WeatherRequest dto, Region region) {

		LocalDateTime nowDateTime = dto.nowDateTime();
		LocalDateTime startDateTime = dto.startDateTime();
		LocalDateTime endDateTime = dto.endDateTime();

		String baseDate = getBaseDate(nowDateTime);
		String baseTime = getBaseTime(nowDateTime);
		String startDate = startDateTime.format(DATE_FORMAT);
		String startTime = convertToFormatTime(startDateTime.getHour());
		String endDate = endDateTime.format(DATE_FORMAT);
		String endTime = convertToFormatTime(endDateTime.getHour());

		return WeatherFilter.of(baseDate, baseTime, startDate, startTime, endDate, endTime,
			region.getNx(), region.getNy());
	}

	private String getBaseTime(LocalDateTime nowDateTime) {

		int hour = nowDateTime.getHour();
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
		return convertToFormatTime(hour);
	}

	private String convertToFormatTime(int hour) {
		return String.format("%02d00", hour);
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
