package com.example.fashionforecastbackend.weather.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record WeatherTotalGroupRequest(

	@NotNull(message = "현재 시간을 입력해주세요.")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	LocalDateTime nowDateTime,

	@NotNull(message = "외출 시작 시간을 입력해주세요.")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	LocalDateTime minStartDateTime,

	@NotNull(message = "외출 끝 시간을 입력해주세요.")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	LocalDateTime maxEndDateTime,

	@Min(value = 33, message = "위도는 33 이상이어야 합니다.")
	@Max(value = 39, message = "위도는 39 이하여야 합니다.")
	double nx,

	@Min(value = 125, message = "경도는 125 이상이어야 합니다.")
	@Max(value = 131, message = "경도는 131 이하여야 합니다.")
	double ny,

	@NotEmpty(message = "시간을 선택해주세요.")
	List<LocalDateTime> selectedTimes
) {
}
