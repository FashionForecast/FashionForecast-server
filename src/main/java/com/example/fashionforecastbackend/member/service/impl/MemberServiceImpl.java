package com.example.fashionforecastbackend.member.service.impl;

import static com.example.fashionforecastbackend.global.error.ErrorCode.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fashionforecastbackend.global.error.exception.MemberNotFoundException;
import com.example.fashionforecastbackend.global.error.exception.TempStageNotFoundException;
import com.example.fashionforecastbackend.member.domain.Member;
import com.example.fashionforecastbackend.member.domain.MemberOutfit;
import com.example.fashionforecastbackend.member.domain.constant.BottomAttribute;
import com.example.fashionforecastbackend.member.domain.constant.TopAttribute;
import com.example.fashionforecastbackend.member.domain.repository.MemberOutfitRepository;
import com.example.fashionforecastbackend.member.domain.repository.MemberRepository;
import com.example.fashionforecastbackend.member.dto.request.MemberGenderRequest;
import com.example.fashionforecastbackend.member.dto.request.MemberOutfitRequest;
import com.example.fashionforecastbackend.member.dto.response.MemberInfoResponse;
import com.example.fashionforecastbackend.member.dto.response.MemberOutfitGroupResponse;
import com.example.fashionforecastbackend.member.dto.response.MemberOutfitResponse;
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
	public void saveMemberOutfit(final MemberOutfitRequest memberOutfitRequest, final Long memberId) {
		final Member member = getById(memberId);
		final TopAttribute topAttribute = TopAttribute.of(memberOutfitRequest.topType(),
			memberOutfitRequest.topColor());
		final BottomAttribute bottomAttribute = BottomAttribute.of(memberOutfitRequest.bottomType(),
			memberOutfitRequest.bottomColor());
		final TempStage tempStage = getByLevel(memberOutfitRequest.tempStageLevel());

		final MemberOutfit memberOutfit = MemberOutfit.builder()
			.topAttribute(topAttribute)
			.bottomAttribute(bottomAttribute)
			.tempStage(tempStage)
			.build();

		member.addMemberOutfit(memberOutfit);

		memberOutfitRepository.save(memberOutfit);
	}

	@Override
	public LinkedList<MemberOutfitGroupResponse> getMemberOutfits(final Long memberId) {
		final LinkedList<MemberOutfit> memberOutfits = memberOutfitRepository.findByMemberIdWithTempStage(memberId);
		final LinkedHashMap<Integer, List<MemberOutfit>> memberOutfitsMap = convertToMap(memberOutfits);
		return convertToList(memberOutfitsMap);
	}

	private LinkedList<MemberOutfitGroupResponse> convertToList(final LinkedHashMap<Integer, List<MemberOutfit>> memberOutfitsMap) {
		LinkedList<MemberOutfitGroupResponse> memberOutfitGroupResponses = new LinkedList<>();
		for (Entry<Integer, List<MemberOutfit>> entry : memberOutfitsMap.entrySet()) {
			final Integer level = entry.getKey();
			final List<MemberOutfit> memberOutfits = entry.getValue();
			final List<MemberOutfitResponse> memberOutfitResponses = memberOutfits.stream()
				.map(MemberOutfitResponse::of)
				.toList();
			final MemberOutfitGroupResponse memberOutfitGroupResponse = MemberOutfitGroupResponse.of(level,
				memberOutfitResponses);
			memberOutfitGroupResponses.add(memberOutfitGroupResponse);
		}
		return memberOutfitGroupResponses;
	}

	private LinkedHashMap<Integer, List<MemberOutfit>> convertToMap(final LinkedList<MemberOutfit> memberOutfits) {
		LinkedHashMap<Integer, List<MemberOutfit>> memberOutfitsMap = new LinkedHashMap<>();
		for (MemberOutfit memberOutfit : memberOutfits) {
			final int level = memberOutfit.getTempStage().getLevel();
			final List<MemberOutfit> values = memberOutfitsMap.getOrDefault(level, new ArrayList<>());
			values.add(memberOutfit);
			memberOutfitsMap.put(level, values);
		}
		return memberOutfitsMap;
	}

	private TempStage getByLevel(final Integer level) {
		return tempStageRepository.findByLevel(level)
			.orElseThrow(() -> new TempStageNotFoundException(TEMP_LEVEL_NOT_FOUND));
	}

	private Member getById(final Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberNotFoundException(NOT_FOUND_MEMBER));
	}


}
