package com.example.fashionforecastbackend.recommend.dto;

import com.example.fashionforecastbackend.outfit.domain.Outfit;

import lombok.Builder;

@Builder
public record RecommendResponse(
	String name,
	String outfitType
) {
	public static RecommendResponse of(Outfit outfit) {
		return RecommendResponse.builder()
			.name(outfit.getName())
			.outfitType(outfit.getOutfitType().toString())
			.build();
	}
}
