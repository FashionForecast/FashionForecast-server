package com.example.fashionforecastbackend.customOutfit.dto.response;

import com.example.fashionforecastbackend.customOutfit.domain.MemberOutfit;
import com.example.fashionforecastbackend.customOutfit.domain.constant.OutfitAttribute;

public record MemberOutfitResponse(
	Long memberOutfitId,
	String topType,
	String topColor,
	String bottomType,
	String bottomColor
) {

	public static MemberOutfitResponse of(final MemberOutfit memberOutfit) {
		final OutfitAttribute topAttribute = memberOutfit.getTopAttribute();
		final OutfitAttribute bottomAttribute = memberOutfit.getBottomAttribute();
		return new MemberOutfitResponse(
			memberOutfit.getId(),
			topAttribute.getType(),
			topAttribute.getColor(),
			bottomAttribute.getType(),
			bottomAttribute.getColor()
		);
	}
}
