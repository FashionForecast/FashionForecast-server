package com.example.fashionforecastbackend.member.service;

import com.example.fashionforecastbackend.member.dto.request.MemberGenderRequest;
import com.example.fashionforecastbackend.member.dto.response.MemberInfoResponse;

public interface MemberService {
	MemberInfoResponse getMemberInfo(Long memberId);

	void saveGender(MemberGenderRequest memberGenderRequest, Long memberId);

	void updateRegion(MemberRegionRequest memberRegionRequest, Long memberId);
}
