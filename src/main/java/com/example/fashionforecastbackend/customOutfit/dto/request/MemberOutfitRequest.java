package com.example.fashionforecastbackend.customOutfit.dto.request;

import jakarta.validation.constraints.NotNull;

public record MemberOutfitRequest(

	@NotNull(message = "상의 타입을 필수 입니다.")
	String topType,
	@NotNull(message = "상의 색상은 필수 입니다.")
	String topColor,
	@NotNull(message = "하의 타입은 필수 입니다.")
	String bottomType,
	@NotNull(message = "하의 색상은 필수 입니다.")
	String bottomColor,
	@NotNull(message = "온도 단계 레벨은 필수 입니다.")
	Integer tempStageLevel
) {

}
