package com.example.fashionforecastbackend.customOutfit.service;

import java.util.LinkedList;
import java.util.List;

import com.example.fashionforecastbackend.customOutfit.dto.request.MemberOutfitRequest;
import com.example.fashionforecastbackend.customOutfit.dto.request.MemberTempStageOutfitRequest;
import com.example.fashionforecastbackend.customOutfit.dto.response.MemberOutfitGroupResponse;
import com.example.fashionforecastbackend.customOutfit.dto.response.MemberOutfitResponse;

public interface MemberOutfitService {

	void saveMemberOutfit(MemberOutfitRequest memberOutfitRequest, Long memberId);

	LinkedList<MemberOutfitGroupResponse> getMemberOutfits(final Long memberId);

	List<MemberOutfitResponse> getMemberTempStageOutfits(final MemberTempStageOutfitRequest request,
		final Long memberId);

	void deleteMemberOutfit(final Long memberOutfitId);

	void updateMemberOutfit(final Long memberOutfitId, final MemberOutfitRequest request);
}
