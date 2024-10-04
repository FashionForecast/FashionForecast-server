package com.example.fashionforecastbackend.member.service.impl;

import static com.example.fashionforecastbackend.global.error.ErrorCode.*;

import org.springframework.stereotype.Service;

import com.example.fashionforecastbackend.global.error.exception.MemberNotFoundException;
import com.example.fashionforecastbackend.member.domain.Member;
import com.example.fashionforecastbackend.member.domain.repository.MemberRepository;
import com.example.fashionforecastbackend.member.dto.request.MemberGenderRequest;
import com.example.fashionforecastbackend.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;

	@Override
	public void saveGender(MemberGenderRequest memberGenderRequest, Long memberId) {
		Member member = getById(memberId);
		member.updateGender(memberGenderRequest.gender());
	}

	private Member getById(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberNotFoundException(NOT_FOUND_MEMBER));
	}
}
