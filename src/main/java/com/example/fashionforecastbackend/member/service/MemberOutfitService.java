package com.example.fashionforecastbackend.member.service;

import java.util.LinkedList;
import java.util.List;

import com.example.fashionforecastbackend.member.dto.request.MemberOutfitRequest;
import com.example.fashionforecastbackend.member.dto.request.MemberTempStageOutfitRequest;
import com.example.fashionforecastbackend.member.dto.response.MemberOutfitGroupResponse;
import com.example.fashionforecastbackend.member.dto.response.MemberOutfitResponse;

public interface MemberOutfitService {

	void saveMemberOutfit(MemberOutfitRequest memberOutfitRequest, Long memberId);

	LinkedList<MemberOutfitGroupResponse> getMemberOutfits(final Long memberId);

	List<MemberOutfitResponse> getMemberTempStageOutfits(final MemberTempStageOutfitRequest request,
		final Long memberId);

	void deleteMemberOutfit(final Long memberOutfitId);

	void updateMemberOutfit(final Long memberOutfitId, final MemberOutfitRequest request);
}
