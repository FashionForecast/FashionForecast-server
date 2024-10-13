package com.example.fashionforecastbackend.member.service.impl;

import static com.example.fashionforecastbackend.global.error.ErrorCode.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fashionforecastbackend.global.error.exception.InvalidOutingTimeException;
import com.example.fashionforecastbackend.global.error.exception.MemberNotFoundException;
import com.example.fashionforecastbackend.global.error.exception.TempStageNotFoundException;
import com.example.fashionforecastbackend.member.domain.Member;
import com.example.fashionforecastbackend.member.domain.repository.MemberOutfitRepository;
import com.example.fashionforecastbackend.member.domain.repository.MemberRepository;
import com.example.fashionforecastbackend.member.dto.request.MemberGenderRequest;
import com.example.fashionforecastbackend.member.dto.request.OutingTimeRequest;
import com.example.fashionforecastbackend.member.dto.request.RegionRequest;
import com.example.fashionforecastbackend.member.dto.request.TempConditionRequest;
import com.example.fashionforecastbackend.member.dto.request.OutingTimeRequest;
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
	public void updateRegion(final RegionRequest request, final Long memberId) {
		final Member member = getById(memberId);
		if (request == null)
			member.getPersonalSetting().updateRegion(null);
		else
			member.getPersonalSetting().updateRegion(request.region());
	}

	@Transactional
	@Override
	public void updateOutingTime(final OutingTimeRequest request, final Long memberId) {
		final Member member = getById(memberId);
		if (request == null)
			member.getPersonalSetting().updateOutingTime(null, null);
		else {
			LocalTime start = request.startTime();
			LocalTime end = request.endTime();
			if(end.isAfter(start)) {
				throw new InvalidOutingTimeException(INVALID_OUTING_TIME);
			}

			member.getPersonalSetting().updateOutingTime(start, end);
		}
	}

	@Transactional
	@Override
	public void updateTempStage(final TempConditionRequest request, final Long memberId) {
		final Member member = getById(memberId);
		if (request == null)
			member.getPersonalSetting().updateTempCondition(null);
		else
			member.getPersonalSetting().updateTempCondition(request.tempCondition());

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
