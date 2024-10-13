package com.example.fashionforecastbackend.member.dto.request;

import java.time.LocalTime;

public record OutingTimeRequest(
	LocalTime startTime,
	LocalTime endTime
) {
}
