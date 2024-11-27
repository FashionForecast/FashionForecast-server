package com.example.fashionforecastbackend.customOutfit.dto.response;

import com.example.fashionforecastbackend.customOutfit.domain.MemberOutfit;
import com.example.fashionforecastbackend.customOutfit.domain.constant.BottomAttribute;
import com.example.fashionforecastbackend.customOutfit.domain.constant.TopAttribute;

public record GuestOutfitResponse(
	Long memberOutfitId,
	String topType,
	String topColor,
	String bottomType,
	String bottomColor
) {

	public static GuestOutfitResponse of(final MemberOutfit memberOutfit) {
		final TopAttribute topAttribute = memberOutfit.getTopAttribute();
		final BottomAttribute bottomAttribute = memberOutfit.getBottomAttribute();
		return new GuestOutfitResponse(
			memberOutfit.getId(),
			topAttribute.getTopType(),
			topAttribute.getTopColor(),
			bottomAttribute.getBottomType(),
			bottomAttribute.getBottomColor()
		);
	}
}
