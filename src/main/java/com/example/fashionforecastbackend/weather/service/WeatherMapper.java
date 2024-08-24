package com.example.fashionforecastbackend.weather.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.fashionforecastbackend.region.domain.Region;
import com.example.fashionforecastbackend.weather.domain.RainType;
import com.example.fashionforecastbackend.weather.domain.Season;
import com.example.fashionforecastbackend.weather.domain.SkyStatus;
import com.example.fashionforecastbackend.weather.domain.Weather;
import com.example.fashionforecastbackend.weather.dto.response.WeatherApi;
import com.example.fashionforecastbackend.weather.dto.response.WeatherForecast;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class WeatherMapper {

	private final ObjectMapper objectMapper;

	public List<WeatherForecast> convertToWeatherResponseDto(Collection<Weather> weathers) {
		return weathers.stream()
			.map(WeatherForecast::from)
			.collect(Collectors.toList());

	}

	public List<WeatherApi> convertToWeatherApiResponse(String responseBody, String node) {
		try {
			JsonNode rootNode = objectMapper.readTree(responseBody);
			JsonNode itemsNode = rootNode.at(node);
			return objectMapper.convertValue(itemsNode, new TypeReference<List<WeatherApi>>() {
			});
		} catch (JsonProcessingException e) {
			log.error("날씨 json을 객체로 변환하는 과정에서 문제가 발생했습니다.", e);
			throw new RuntimeException(e);
		}
	}

	public Collection<Weather> convertToWeathers(List<WeatherApi> responses, Region region) {
		LocalDate now = LocalDate.now();
		String tomorrow = now.plusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		Map<String, Weather> weatherMap = new HashMap<>();
		// 오늘, 내일 날씨 데이터만 가져온다
		for (WeatherApi response : responses) {
			if (tomorrow.compareTo(response.fcstDate()) < 0) {
				break;
			}
			setWeatherForecast(response, region, weatherMap);
		}
		return weatherMap.values();
	}

	private void setWeatherForecast(WeatherApi response, Region region, Map<String, Weather> weatherMap) {
		String baseDate = response.baseDate();
		String baseTime = response.baseTime();
		String fcstDate = response.fcstDate();
		String fcstTime = response.fcstTime();
		int nx = response.nx();
		int ny = response.ny();
		String key = getKey(fcstDate, fcstTime, nx, ny);
		Weather weather = weatherMap.getOrDefault(key,
			Weather.builder()
				.baseDate(baseDate)
				.baseTime(baseTime)
				.fcstDate(fcstDate)
				.fcstTime(fcstTime)
				.nx(region.getNx())
				.ny(region.getNy())
				.build());

		setCategory(response, weather);
		setSeason(weather);

		weatherMap.put(key, weather);

	}

	private void setSeason(Weather weather) {

		String fcstDate = weather.getFcstDate();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate localDate = LocalDate.parse(fcstDate, formatter);
		int month = localDate.getMonthValue();
		Season season = Season.fromMonth(month);
		weather.setSeason(season);

	}

	private void setCategory(WeatherApi response, Weather weather) {
		switch (response.category()) {
			case "POP":
				weather.setPop(response.fcstValue());
				break;
			case "PTY":
				weather.setRainType(RainType.from(response.fcstValue()));
				break;
			case "REH":
				weather.setReh(response.fcstValue());
				break;
			case "SKY":
				weather.setSkyStatus(SkyStatus.from(response.fcstValue()));
				break;
			case "WSD":
				weather.setWsd(response.fcstValue());
				break;
			case "PCP":
				String value = response.fcstValue();
				if (Objects.equals(value, "강수없음")) {
					value = "0mm";
				}
				if (!value.contains("미만")) {
					value = "0.5mm";
				}
				weather.setPcp(value);
				break;
			case "TMP":
				weather.setTmp(response.fcstValue());
				break;
		}
	}

	private String getKey(String fcstDate, String fcstTime, int nx, int ny) {
		StringBuilder sb = new StringBuilder();
		sb.append(fcstDate);
		sb.append(fcstTime);
		sb.append(nx);
		sb.append(ny);
		return sb.toString();
	}

}
