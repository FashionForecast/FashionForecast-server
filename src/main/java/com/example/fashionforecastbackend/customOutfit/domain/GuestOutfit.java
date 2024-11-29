package com.example.fashionforecastbackend.customOutfit.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GuestOutfit {

	private int tempStageLevel;
	private String topType;
	private String topColor;
	private String bottomType;
	private String bottomColor;

	@Builder
	public GuestOutfit(int tempStageLevel, String topType, String topColor, String bottomType, String bottomColor) {
		this.tempStageLevel = tempStageLevel;
		this.topType = topType;
		this.topColor = topColor;
		this.bottomType = bottomType;
		this.bottomColor = bottomColor;
	}
}
