package com.example.fashionforecastbackend.recommend.dto;

import java.time.LocalDateTime;

import com.example.fashionforecastbackend.recommend.domain.TempCondition;

public record RecommendRequest(
	int nx,
	int ny,
	LocalDateTime startTime,
	LocalDateTime endTime,
	TempCondition tempCondition
) {
}
