package com.example.fashionforecastbackend.weather.domain;

import java.util.Arrays;
import java.util.Objects;

public enum SkyStatus {

	CLEAR("1", "맑음"),
	PARTLY_CLOUDY("3", "구름많음"),
	CLOUDY("4", "흐림");

	private final String code;
	private final String description;

	SkyStatus(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public static SkyStatus from(String code) {
		return Arrays.stream(SkyStatus.values())
			.filter(skyStatus -> Objects.equals(code, skyStatus.code))
			.findAny()
			.orElse(SkyStatus.CLEAR);
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}
}
