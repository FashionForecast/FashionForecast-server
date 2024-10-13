package com.example.fashionforecastbackend.member.service;

import java.util.LinkedList;

import com.example.fashionforecastbackend.member.dto.request.MemberGenderRequest;
import com.example.fashionforecastbackend.member.dto.request.MemberOutfitRequest;
import com.example.fashionforecastbackend.member.dto.response.MemberInfoResponse;
import com.example.fashionforecastbackend.member.dto.response.MemberOutfitGroupResponse;

public interface MemberService {
	MemberInfoResponse getMemberInfo(Long memberId);
	void saveGender(MemberGenderRequest memberGenderRequest, Long memberId);
	void saveMemberOutfit(MemberOutfitRequest memberOutfitRequest, Long memberId);
	LinkedList<MemberOutfitGroupResponse> getMemberOutfits(final Long memberId);
}
