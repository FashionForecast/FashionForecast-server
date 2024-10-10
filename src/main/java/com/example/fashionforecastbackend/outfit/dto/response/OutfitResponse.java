package com.example.fashionforecastbackend.outfit.dto.response;

import com.example.fashionforecastbackend.outfit.domain.Outfit;
import com.example.fashionforecastbackend.outfit.domain.OutfitType;

public record OutfitResponse(
	String name,
	OutfitType outfitType
) {
	public static OutfitResponse of(final Outfit outfit) {
		return new OutfitResponse(outfit.getName(), outfit.getOutfitType());
	}
}
