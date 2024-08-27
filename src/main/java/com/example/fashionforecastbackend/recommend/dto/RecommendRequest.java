package com.example.fashionforecastbackend.recommend.dto;

import org.hibernate.validator.constraints.Range;

import com.example.fashionforecastbackend.recommend.domain.TempCondition;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record RecommendRequest(
	@NotNull(message = "최대/최소 기온을 입력해주세요.")
	@Range(min = -50, max = 50, message = "-50~50 사이의 값을 입력해주세요.")
	int extremumTmp,
	@NotNull(message = "최대 기온과 최소 기온의 차를 입력해주세요")
	@PositiveOrZero(message = "0이상의 값을 입력해주세요.")
	int maxMinTmpDiff,
	@NotNull(message = "최대 강수확률을 입력해주세요.")
	@Range(min = 0, max = 100, message = "0~100 사이의 값을 입력해주세요.")
	int maximumPop,
	@NotNull(message = "최대 강수량을 입력해주세요.")
	@PositiveOrZero(message = "0이상의 값을 입력해주세요.")
	double maximumPcp,
	@NotNull(message = "옷차림 설정 옵션을 입력해주세요. COOL/NORMAL/WARM")
	TempCondition tempCondition
) {
}
