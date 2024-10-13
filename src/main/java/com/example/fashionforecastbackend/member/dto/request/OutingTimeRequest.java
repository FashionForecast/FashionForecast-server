package com.example.fashionforecastbackend.member.dto.request;

import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

public record OutingTimeRequest(
	@NotNull(message = "외출 시작 시간을 입력해주세요")
	LocalTime startTime,
	@NotNull(message = "외출 끝 시간을 입력해주세요")
	LocalTime endTime
) {
}
