package com.example.fashionforecastbackend.member.service.impl;

import static com.example.fashionforecastbackend.global.error.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fashionforecastbackend.global.error.exception.MemberNotFoundException;
import com.example.fashionforecastbackend.member.domain.Member;
import com.example.fashionforecastbackend.member.domain.repository.MemberOutfitRepository;
import com.example.fashionforecastbackend.member.domain.repository.MemberRepository;
import com.example.fashionforecastbackend.member.dto.request.MemberGenderRequest;
import com.example.fashionforecastbackend.member.dto.response.MemberInfoResponse;
import com.example.fashionforecastbackend.member.service.MemberService;
import com.example.fashionforecastbackend.tempStage.domain.TempStage;
import com.example.fashionforecastbackend.tempStage.domain.repository.TempStageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;
	private final MemberOutfitRepository memberOutfitRepository;
	private final TempStageRepository tempStageRepository;

	@Transactional
	@Override
	public MemberInfoResponse getMemberInfo(final Long memberId) {
		Member member = getById(memberId);
		return MemberInfoResponse.of(member);
	}

	@Transactional
	@Override
	public void saveGender(final MemberGenderRequest memberGenderRequest, final Long memberId) {
		Member member = getById(memberId);
		member.updateGender(memberGenderRequest.gender());
	}

	@Transactional
	@Override
	public void saveRegion(MemberRegionRequest memberRegionRequest, Long memberId) {
		Member member = getById(memberId);
		member.getPersonalSetting().updateRegion(memberRegionRequest.region());
	}

	private Member getById(final Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberNotFoundException(NOT_FOUND_MEMBER));
	}

}
