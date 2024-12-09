package com.example.fashionforecastbackend.customOutfit.dto.request;

import jakarta.validation.constraints.NotNull;

public record DeleteGuestOutfitRequest(
	@NotNull(message = "uuid는 필수 입니다.")
	String uuid,
	@NotNull(message = "온도 단계 레벨은 필수 입니다.")
	int tempStageLevel
) {
}
