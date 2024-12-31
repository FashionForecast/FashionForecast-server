package com.example.fashionforecastbackend.weather.domain;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum Season {

	SUMMER(5, 9),
	WINTER(10, 16);

	private final int startMonth;
	private final int endMonth;

	Season(int startMonth, int endMonth) {
		this.startMonth = startMonth;
		this.endMonth = endMonth;
	}

	public static Season fromMonth(int month) {
		return Arrays.stream(Season.values())
			.filter(season -> season.startMonth <= month && season.endMonth >= month)
			.findAny()
			.orElse(WINTER);
	}
}
