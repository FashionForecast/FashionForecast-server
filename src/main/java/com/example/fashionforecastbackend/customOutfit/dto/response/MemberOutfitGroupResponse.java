package com.example.fashionforecastbackend.customOutfit.dto.response;

import java.util.List;

public record MemberOutfitGroupResponse(
	Integer tempStageLevel,
	List<MemberOutfitResponse> memberOutfits
) {

	public static MemberOutfitGroupResponse of(final Integer tempStageLevel, final List<MemberOutfitResponse> memberOutfits) {
		return new MemberOutfitGroupResponse(tempStageLevel, memberOutfits);
	}
}
