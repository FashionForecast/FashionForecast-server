package com.example.fashionforecastbackend.customOutfit.dto.response;

import com.example.fashionforecastbackend.customOutfit.domain.GuestOutfit;

public record GuestOutfitResponse(
	int tempStageLevel,
	String topType,
	String topColor,
	String bottomType,
	String bottomColor
) {

	public static GuestOutfitResponse of(final GuestOutfit guestOutfit) {
		return new GuestOutfitResponse(
			guestOutfit.getTempStageLevel(),
			guestOutfit.getTopType(),
			guestOutfit.getTopColor(),
			guestOutfit.getBottomType(),
			guestOutfit.getBottomColor()
		);
	}
}
