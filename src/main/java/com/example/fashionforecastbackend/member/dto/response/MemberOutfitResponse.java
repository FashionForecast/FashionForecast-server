package com.example.fashionforecastbackend.member.dto.response;

import com.example.fashionforecastbackend.member.domain.MemberOutfit;
import com.example.fashionforecastbackend.member.domain.constant.BottomAttribute;
import com.example.fashionforecastbackend.member.domain.constant.TopAttribute;

public record MemberOutfitResponse(
	Long memberOutfitId,
	String topType,
	String topColor,
	String bottomType,
	String bottomColor
) {

	public static MemberOutfitResponse of(final MemberOutfit memberOutfit) {
		final TopAttribute topAttribute = memberOutfit.getTopAttribute();
		final BottomAttribute bottomAttribute = memberOutfit.getBottomAttribute();
		return new MemberOutfitResponse(
			memberOutfit.getId(),
			topAttribute.getTopType(),
			topAttribute.getTopColor(),
			bottomAttribute.getBottomType(),
			bottomAttribute.getBottomColor()
		);
	}
}
