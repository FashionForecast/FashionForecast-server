package com.example.fashionforecastbackend.outfit.dto.response;

import java.util.List;

public record OutfitGroupResponse(
	List<OutfitResponse> tops,
	List<OutfitResponse> bottoms
) {
	public static OutfitGroupResponse of(List<OutfitResponse> tops, List<OutfitResponse> bottoms) {
		return new OutfitGroupResponse(tops, bottoms);
	}
}
