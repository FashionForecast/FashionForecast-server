package com.example.fashionforecastbackend.customOutfit.domain.constant;

import com.example.fashionforecastbackend.outfit.domain.OutfitType;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutfitAttribute {

	private OutfitType outfitType;
	private String type;
	private String color;

	private OutfitAttribute(final String type, final String color) {
		this.type = type;
		this.color = color;
	}

	public static OutfitAttribute of(final OutfitType outfitType, final String type, final String color) {
		return new OutfitAttribute(type, color);
	}

}
