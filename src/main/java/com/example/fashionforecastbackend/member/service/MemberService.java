package com.example.fashionforecastbackend.member.service;

import com.example.fashionforecastbackend.member.dto.request.MemberGenderRequest;

public interface MemberService {
	void saveGender(MemberGenderRequest memberGenderRequest, Long memberId);
}
