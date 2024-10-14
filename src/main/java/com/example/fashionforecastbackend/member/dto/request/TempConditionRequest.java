package com.example.fashionforecastbackend.member.dto.request;

import com.example.fashionforecastbackend.recommend.domain.TempCondition;

import jakarta.validation.constraints.NotNull;

public record TempConditionRequest(
	@NotNull(message = "옷차림 두께를 입력해주세요. COOL/NORMAL/WARM")
	TempCondition tempCondition
) {
}
