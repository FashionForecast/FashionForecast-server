package com.example.fashionforecastbackend.recommend.dto;

import com.example.fashionforecastbackend.recommend.domain.TempCondition;

import jakarta.validation.constraints.NotNull;

public record RecommendRequest(
	@NotNull
	int extremumTmp,
	@NotNull
	int maxMinTmpDiff,
	@NotNull
	int maximumPop,
	@NotNull
	double maximumPcp,
	@NotNull
	TempCondition tempCondition
) {
}
