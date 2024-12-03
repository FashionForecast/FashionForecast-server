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
import com.example.fashionforecastbackend.weather.api.RestClientWeatherRequesterV2;
import com.example.fashionforecastbackend.weather.domain.Season;
import com.example.fashionforecastbackend.weather.domain.Weather;
import com.example.fashionforecastbackend.weather.domain.repository.WeatherRepository;
import com.example.fashionforecastbackend.weather.dto.request.WeatherFilter;
import com.example.fashionforecastbackend.weather.dto.request.WeatherRequest;
import com.example.fashionforecastbackend.weather.dto.response.WeatherApiV2;
import com.example.fashionforecastbackend.weather.dto.response.WeatherForecast;
import com.example.fashionforecastbackend.weather.dto.response.WeatherResponse;
import com.example.fashionforecastbackend.weather.service.WeatherMapper;
import com.example.fashionforecastbackend.weather.service.WeatherMapperV2;
import com.example.fashionforecastbackend.weather.service.WeatherService;
import com.example.fashionforecastbackend.weather.util.WeatherDateTimeValidator;

import lombok.RequiredArgsConstructor;

@Service("weatherServiceImplV2")
@RequiredArgsConstructor
public class WeatherServiceImplV2 implements WeatherService {

	private static final int DEFAULT_FORECAST_DAYS = 3;
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmm");

	private final WeatherRepository weatherRepository;
	private final WeatherMapper weatherMapper;
	private final RegionRepository regionRepository;
	private final RestClientWeatherRequesterV2 restClientWeatherRequesterV2;
	private final WeatherDateTimeValidator validator;

	@Override
	public WeatherResponse getWeather(WeatherRequest dto) {
//		validateDtoDateTime(dto);
		final Region region = findRegion(dto.nx(), dto.ny());
		final WeatherFilter weatherFilter = getWeatherFilter(dto, region);

		List<Weather> weathers = weatherRepository.findWeather(weatherFilter);

		if (weathers.isEmpty() || isUnderSize(weathers, weatherFilter)) {
			WeatherApiV2 response = restClientWeatherRequesterV2.getWeatherForecast(
				dto.nx(),
				dto.ny(),
				DEFAULT_FORECAST_DAYS
			);
			weathers = WeatherMapperV2.mapToWeatherForecast(response, region);
			weatherRepository.saveAll(weathers);
		}

		weathers = getFilteredWeathers(weathers, weatherFilter);
		sortWeathersByDateTime(weathers);

		Season season = weathers.get(0).getSeason();
		List<WeatherForecast> weatherForecasts = weatherMapper.convertToWeatherResponseDto(weathers);

		return getWeatherResponse(weatherForecasts, season);
	}

	@Async
	@Transactional(propagation = REQUIRES_NEW)
	@Scheduled(cron = "0 15 * * * *")
	public void insertWeather() {
		LocalDateTime now = LocalDateTime.now();
		final String baseDate = now.format(DATE_FORMATTER);
		final String baseTime = now.withMinute(0).format(TIME_FORMATTER);
		final List<Region> regions = regionRepository.findByCities(List.of("서울특별시", "경기도"));
		final List<Weather> weathers = regions.stream()
			.map(region -> getWeathersByBaseDateTimeAndRegion(baseDate, baseTime, region))
			.flatMap(Collection::stream)
			.toList();
		if (!weathers.isEmpty()) {
			weatherRepository.saveAll(weathers);
		}
	}

	private List<Weather> getWeathersByBaseDateTimeAndRegion(final String baseDate, final String baseTime, final Region region) {
		List<Weather> weathers = weatherRepository.findByBaseDateAndBaseTimeAndNxAndNy(
			baseDate, baseTime, region.getNx(), region.getNy());
		if (weathers.isEmpty()) {
			WeatherApiV2 response = restClientWeatherRequesterV2.getWeatherForecast(region.getNx(), region.getNy(), DEFAULT_FORECAST_DAYS);
			weathers = WeatherMapperV2.mapToWeatherForecast(response, region);
		}
		return weathers;
	}


	private boolean isUnderSize(List<Weather> weathers, WeatherFilter weatherFilter) {
		int startTime = Integer.parseInt(weatherFilter.startTime().substring(2));
		int endTime = Integer.parseInt(weatherFilter.endTime().substring(2));
		int requestSize = (endTime - startTime) + 1;
		return (weathers.size() < requestSize);
	}

	private Region findRegion(double nx, double ny) {

		Region region = regionRepository.findByNxAndNy(nx, ny).orElse(null);
		if (Objects.equals(region, null)) {
			List<Region> regions = regionRepository.findAll();
			Collections.sort(regions, (r1, r2) -> {
				double diff1 = Math.abs(r1.getNx() - nx) + Math.abs(r1.getNy() - ny);
				double diff2 = Math.abs(r2.getNx() - nx) + Math.abs(r2.getNy() - ny);
				return Double.compare(diff1, diff2);
			});
			region = regions.get(0);
		}
		return region;
	}

	private WeatherFilter getWeatherFilter(WeatherRequest dto, Region region) {

		LocalDateTime baseDateTime = getBaseDateTime(dto.nowDateTime());
		LocalDateTime startDateTime = dto.startDateTime();
		LocalDateTime endDateTime = dto.endDateTime();

		String baseDate = baseDateTime.format(DATE_FORMATTER);
		String baseTime = baseDateTime.format(TIME_FORMATTER);
		String startDate = startDateTime.format(DATE_FORMATTER);
		String startTime = convertToFormatTime(startDateTime.getHour());
		String endDate = endDateTime.format(DATE_FORMATTER);
		String endTime = convertToFormatTime(endDateTime.getHour());

		return WeatherFilter.of(baseDate, baseTime, startDate, startTime, endDate, endTime,
			region.getNx(), region.getNy());
	}

	private String convertToFormatTime(int hour) {
		return String.format("%02d00", hour);
	}

	private boolean isInDateTime(Weather weather, WeatherFilter weatherFilter) {

		String startDateTime = weatherFilter.startDate() + weatherFilter.startTime();
		String endDateTime = weatherFilter.endDate() + weatherFilter.endTime();
		String fcstDateTime = weather.getFcstDate() + weather.getFcstTime();

		return fcstDateTime.compareTo(startDateTime) >= 0 &&
			fcstDateTime.compareTo(endDateTime) <= 0;
	}

	private LocalDateTime getBaseDateTime(LocalDateTime nowDateTime) {
		int hour = nowDateTime.getHour();
		final int minute = nowDateTime.getMinute();
		if (hour == 0 && minute <= 15) {
			nowDateTime = nowDateTime.minusDays(1).withHour(23);
		}
		return nowDateTime.withMinute(0);
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
			.distinct()
			.collect(Collectors.toList());
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

	private void validateDtoDateTime(WeatherRequest dto) {

		LocalDateTime nowDateTime = dto.nowDateTime();
		LocalDateTime startDateTime = dto.startDateTime();
		LocalDateTime endDateTime = dto.endDateTime();

		validator.validateNowDateTime(nowDateTime);
		validator.validateStartDateTime(nowDateTime, startDateTime, endDateTime);
		validator.validateEndDateTime(nowDateTime, startDateTime, endDateTime);

	}
}
