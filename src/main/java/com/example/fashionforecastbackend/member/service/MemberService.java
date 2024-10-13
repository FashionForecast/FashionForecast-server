package com.example.fashionforecastbackend.member.service;

import com.example.fashionforecastbackend.member.dto.request.MemberGenderRequest;
import com.example.fashionforecastbackend.member.dto.request.MemberOutfitRequest;
import com.example.fashionforecastbackend.member.dto.request.OutingTimeRequest;
import com.example.fashionforecastbackend.member.dto.request.RegionRequest;
import com.example.fashionforecastbackend.member.dto.response.MemberInfoResponse;

public interface MemberService {
	MemberInfoResponse getMemberInfo(Long memberId);

	void saveGender(MemberGenderRequest memberGenderRequest, Long memberId);

	void updateRegion(RegionRequest regionRequest, Long memberId);

	void updateOutingTime(OutingTimeRequest memberOutingRequest, Long memberId);
}
