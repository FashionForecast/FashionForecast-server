package com.example.fashionforecastbackend.recommend.dto;

import java.time.LocalDateTime;

public record RecommendRequest(
	int nx,
	int ny,
	LocalDateTime startTime,
	LocalDateTime endTime
) {
}
