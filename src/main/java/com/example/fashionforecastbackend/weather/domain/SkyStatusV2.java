package com.example.fashionforecastbackend.weather.domain;

import java.util.Arrays;

public enum SkyStatusV2 {
	CLEAR(SkyStatus.CLEAR,1000, "맑음"),
	PARTLY_CLOUDY(SkyStatus.PARTLY_CLOUDY,1003, "구름많음"),
	CLOUDY(SkyStatus.CLOUDY,1006, "흐림");

	private final SkyStatus skyStatus;
	private final int code;
	private final String description;

	SkyStatusV2(final SkyStatus skyStatus, final int code, final String description) {
		this.skyStatus = skyStatus;
		this.code = code;
		this.description = description;
	}

	public static SkyStatus getSkyStatusByCode(final int code) {
		return SkyStatusV2.findByCode(code).skyStatus;
	}

	private static SkyStatusV2 findByCode(final int code) {
		return Arrays.stream(SkyStatusV2.values())
			.filter(skyStatusV2 -> skyStatusV2.code == code)
			.findAny()
			.orElse(CLOUDY);
	}
}
