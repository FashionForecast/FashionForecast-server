package com.example.fashionforecastbackend.member.domain.constant;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TopAttribute {

	private String topType;
	private String topColor;

	private TopAttribute(final String topType, final String topColor) {
		this.topType = topType;
		this.topColor = topColor;
	}

	public static TopAttribute of(final String topType, final String topColor) {
		return new TopAttribute(topType, topColor);
	}

}
