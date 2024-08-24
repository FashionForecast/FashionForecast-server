package com.example.fashionforecastbackend.weather.dto.request;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record WeatherRequest(

	@NotNull(message = "현재 시간을 입력해주세요.")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	LocalDateTime nowDateTime,

	@NotNull(message = "외출 시작 시간을 입력해주세요.")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	LocalDateTime startDateTime,

	@NotNull(message = "외출 끝 시간을 입력해주세요.")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	LocalDateTime endDateTime,

	@Min(value = 1, message = "위도는 1 이상이어야 합니다.")
	@Max(value = 999, message = "위도는 999 이하여야 합니다.")
	int nx,

	@Min(value = 1, message = "경도는 1 이상이어야 합니다.")
	@Max(value = 999, message = "경도는 999 이하여야 합니다.")
	int ny
) {
}
