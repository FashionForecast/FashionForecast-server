package com.example.fashionforecastbackend.weather.domain;

import java.util.Arrays;
import java.util.Objects;

public enum RainType {
	NONE("0", "없음"),
	RAIN("1", "비"),
	RAIN_AND_SNOW("2", "비/눈"),
	SNOW("3", "눈"),
	SHOWER("4", "소나기"),
	RAIN_DROP("5", "빗방울"),
	RAIN_AND_SNOW_FLURRIES("6", "빗방울눈날림"),
	SNOW_FLURRIES("7", "눈날림");

	private final String code;
	private final String description;

	RainType(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public static RainType from(String code) {
		return Arrays.stream(RainType.values())
			.filter(rainType -> Objects.equals(code, rainType.code))
			.findAny()
			.orElse(RainType.NONE);
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}
}
