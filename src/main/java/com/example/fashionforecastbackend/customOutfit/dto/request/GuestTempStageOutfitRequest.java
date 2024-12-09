package com.example.fashionforecastbackend.customOutfit.dto.request;

import com.example.fashionforecastbackend.recommend.domain.TempCondition;

import jakarta.validation.constraints.NotNull;

public record GuestTempStageOutfitRequest(

	@NotNull(message = "uuid는 필수 입니다.")
	String uuid,
	@NotNull(message = "현재 최저 혹은 최고 기온은 필수 입니다.")
	int extremumTmp,
	@NotNull(message = "옷차림 설정 옵션을 입력해주세요. COOL/NORMAL/WARM")
	TempCondition tempCondition

) {
}
