package com.example.fashionforecastbackend.customOutfit.dto.response;

import java.util.List;

public record GuestOutfitGroupResponse(
	Integer tempStageLevel,
	List<GuestOutfitResponse> memberOutfits
) {

	public static GuestOutfitGroupResponse of(final Integer tempStageLevel, final List<GuestOutfitResponse> memberOutfits) {
		return new GuestOutfitGroupResponse(tempStageLevel, memberOutfits);
	}
}
