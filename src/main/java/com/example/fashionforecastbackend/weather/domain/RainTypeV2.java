package com.example.fashionforecastbackend.weather.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum RainTypeV2 {
	NONE(RainType.NONE, Collections.emptyList(), "없음"),
	RAIN(RainType.RAIN, List.of(1063, 1072, 1180, 1183, 1186, 1189, 1192, 1195, 1198, 1201, 1273, 1276), "비"),
	RAIN_AND_SNOW(RainType.RAIN_AND_SNOW, List.of(1069, 1204, 1207, 1249, 1252), "비/눈"),
	SNOW(RainType.SNOW, List.of(1066, 1210, 1213, 1216, 1219, 1222, 1225, 1237, 1255, 1258, 1261, 1264, 1279, 1282),
		"눈"),
	SHOWER(RainType.SHOWER, List.of(1087, 1240, 1243, 1246), "소나기"),
	RAIN_DROP(RainType.RAIN_DROP, List.of(1150, 1153, 1168, 1171), "빗방울"),
	RAIN_AND_SNOW_FLURRIES(RainType.RAIN_AND_SNOW_FLURRIES, List.of(1069, 1249, 1252), "빗방울눈날림"),
	SNOW_FLURRIES(RainType.SNOW_FLURRIES, List.of(1114, 1117), "눈날림");

	private final RainType rainType;
	private final List<Integer> codes;
	private final String description;

	RainTypeV2(final RainType rainType, final List<Integer> codes, final String description) {
		this.rainType = rainType;
		this.codes = codes;
		this.description = description;
	}

	public static RainType getRainTypeByCode(final int code) {
		return RainTypeV2.findByCode(code).rainType;
	}

	private static RainTypeV2 findByCode(final int code) {
		return Arrays.stream(RainTypeV2.values())
			.filter(rainTypeV2 -> rainTypeV2.codes.contains(code))
			.findAny()
			.orElse(NONE);
	}
}
