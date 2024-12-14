package com.example.fashionforecastbackend.customOutfit.domain.constant;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BottomAttribute {

	private String bottomType;
	private String bottomColor;

	private BottomAttribute(final String bottomType, final String bottomColor) {
		this.bottomType = bottomType;
		this.bottomColor = bottomColor;
	}

	public static BottomAttribute of(final String bottomType, final String bottomColor) {
		return new BottomAttribute(bottomType, bottomColor);
	}

}
