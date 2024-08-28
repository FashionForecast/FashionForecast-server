package com.example.fashionforecastbackend.recommend.dto;

import java.util.List;

import com.example.fashionforecastbackend.outfit.domain.OutfitType;

import lombok.Builder;

@Builder
public record RecommendResponse(
	List<String> names,
	OutfitType outfitType
) {
	public static RecommendResponse of(List<String> names, OutfitType outfitType) {
		return RecommendResponse.builder()
			.names(names)
			.outfitType(outfitType)
			.build();
	}
}
