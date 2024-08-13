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
import com.example.fashionforecastbackend.weather.domain.SkyStatus;
import com.example.fashionforecastbackend.weather.domain.Weather;
import com.example.fashionforecastbackend.weather.dto.WeatherApiResponse;
import com.example.fashionforecastbackend.weather.dto.WeatherResponseDto;
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

	private final String ULTRA_FORECAST_RAINFALL_AMOUNT = "RN1";
	private final String ULTRA_FORECAST_TMP = "T1H";
	private final String SHORT_FORECAST_RAINFALL_AMOUNT = "PCP";
	private final String SHORT_FORECAST_TMP = "TMP";
	private final ObjectMapper objectMapper;

	public List<WeatherResponseDto> convertToWeatherResponseDto(Collection<Weather> weathers) {
		return weathers.stream()
			.map(WeatherResponseDto::from)
			.sorted((d1, d2) -> {
				String dateTime1 = d1.fcstDate() + d1.fcstTime();
				String dateTime2 = d2.fcstDate() + d2.fcstTime();
				return dateTime1.compareTo(dateTime2);
			})
			.collect(Collectors.toList());

	}

	public List<WeatherApiResponse> convertToWeatherApiResponse(String responseBody, String node) {
		try {
			JsonNode rootNode = objectMapper.readTree(responseBody);
			JsonNode itemsNode = rootNode.at(node);
			return objectMapper.convertValue(itemsNode, new TypeReference<List<WeatherApiResponse>>() {
			});
		} catch (JsonProcessingException e) {
			log.error("날씨 json을 객체로 변환하는 과정에서 문제가 발생했습니다.", e);
			throw new RuntimeException(e);
		}
	}

	public Collection<Weather> convertToWeathers(List<WeatherApiResponse> responses, Region region,
		String forecastType) {
		LocalDate now = LocalDate.now();
		String tomorrow = now.plusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		Map<String, Weather> weatherMap = new HashMap<>();
		// 오늘, 내일 날씨 데이터만 가져온다
		for (WeatherApiResponse response : responses) {
			if (tomorrow.compareTo(response.fcstDate()) < 0) {
				break;
			}
			setWeatherCategory(response, region, weatherMap, forecastType);
		}
		return weatherMap.values();
	}

	private void setWeatherCategory(WeatherApiResponse response, Region region, Map<String, Weather> weatherMap,
		String forecastType) {
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
		String rainfallAmount = "";
		String tmp = "";

		if (Objects.equals(forecastType, "ultra")) {
			rainfallAmount = ULTRA_FORECAST_RAINFALL_AMOUNT;
			tmp = ULTRA_FORECAST_TMP;
		} else if (Objects.equals(forecastType, "short")) {
			rainfallAmount = SHORT_FORECAST_RAINFALL_AMOUNT;
			tmp = SHORT_FORECAST_TMP;
		}

		switch (response.category()) {
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
			default:
				if (Objects.equals(response.category(), rainfallAmount)) {
					weather.setRainfallAmount(response.fcstValue());
				} else if (Objects.equals(response.category(), tmp)) {
					weather.setTmp(response.fcstValue());
				}
		}

		weatherMap.put(key, weather);

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
