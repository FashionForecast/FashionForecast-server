package com.example.fashionforecastbackend.member.service.impl;

import static com.example.fashionforecastbackend.global.error.ErrorCode.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fashionforecastbackend.global.error.exception.InvalidWeatherRequestException;
import com.example.fashionforecastbackend.global.error.exception.MemberNotFoundException;
import com.example.fashionforecastbackend.global.error.exception.MemberOutfitLimitExceededException;
import com.example.fashionforecastbackend.global.error.exception.TempStageNotFoundException;
import com.example.fashionforecastbackend.member.domain.Member;
import com.example.fashionforecastbackend.member.domain.MemberOutfit;
import com.example.fashionforecastbackend.member.domain.constant.BottomAttribute;
import com.example.fashionforecastbackend.member.domain.constant.TopAttribute;
import com.example.fashionforecastbackend.member.domain.repository.MemberOutfitRepository;
import com.example.fashionforecastbackend.member.domain.repository.MemberRepository;
import com.example.fashionforecastbackend.member.dto.request.MemberGenderRequest;
import com.example.fashionforecastbackend.member.dto.request.MemberOutfitRequest;
import com.example.fashionforecastbackend.member.dto.request.MemberTempStageOutfitRequest;
import com.example.fashionforecastbackend.member.dto.response.MemberInfoResponse;
import com.example.fashionforecastbackend.member.dto.response.MemberOutfitGroupResponse;
import com.example.fashionforecastbackend.member.dto.response.MemberOutfitResponse;
import com.example.fashionforecastbackend.member.service.MemberService;
import com.example.fashionforecastbackend.recommend.domain.TempCondition;
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
		final TempStage tempStage = getTempStageByLevel(memberOutfitRequest.tempStageLevel());
		validateCount(tempStage.getId(), memberId);
		final TopAttribute topAttribute = TopAttribute.of(memberOutfitRequest.topType(),
			memberOutfitRequest.topColor());
		final BottomAttribute bottomAttribute = BottomAttribute.of(memberOutfitRequest.bottomType(),
			memberOutfitRequest.bottomColor());


		final MemberOutfit memberOutfit = MemberOutfit.builder()
			.topAttribute(topAttribute)
			.bottomAttribute(bottomAttribute)
			.tempStage(tempStage)
			.build();

		member.addMemberOutfit(memberOutfit);
		memberOutfitRepository.save(memberOutfit);
	}

	@Transactional
	@Override
	public void deleteMemberOutfit(final Long memberOutfitId) {
		memberOutfitRepository.deleteById(memberOutfitId);
	}

	@Override
	public LinkedList<MemberOutfitGroupResponse> getMemberOutfits(final Long memberId) {
		final List<MemberOutfit> memberOutfits = memberOutfitRepository.findByMemberIdWithTempStage(memberId);
		final LinkedHashMap<Integer, List<MemberOutfit>> memberOutfitsMap = convertToMap(memberOutfits);
		return convertToList(memberOutfitsMap);
	}

	@Override
	public List<MemberOutfitResponse> getMemberTempStageOutfits(final MemberTempStageOutfitRequest request,
		final Long memberId) {
		final Member member = getById(memberId);
		validateTempCondition(request);
		final TempStage tempStage = getTempStageByRequest(request);
		final List<MemberOutfit> outfits = memberOutfitRepository.findByMemberIdAndTempStageId(member.getId(),
			tempStage.getId());
		return outfits.stream()
			.map(MemberOutfitResponse::of)
			.toList();
	}

	private void validateCount(final Long tempStageId, final Long memberId) {
		final Integer count = memberOutfitRepository.countByTempStageIdAndMemberId(tempStageId, memberId);
		if (count >= 4) {
			throw new MemberOutfitLimitExceededException(MEMBER_OUTFIT_COUNT_EXCEEDED);
		}
	}

	private TempStage getTempStageByRequest(final MemberTempStageOutfitRequest request) {
		if (request.tempCondition() == TempCondition.WARM) {
			return tempStageRepository.findByWeatherAndWarmOption(request.extremumTmp())
				.orElseThrow(() -> new TempStageNotFoundException(TEMP_LEVEL_NOT_FOUND));
		}
		if (request.tempCondition() == TempCondition.NORMAL) {
			return tempStageRepository.findByWeather(request.extremumTmp())
				.orElseThrow(() -> new TempStageNotFoundException(TEMP_LEVEL_NOT_FOUND));
		}
		if (request.tempCondition() == TempCondition.COOL) {
			return tempStageRepository.findByWeatherAndCoolOption(request.extremumTmp())
				.orElseThrow(() -> new TempStageNotFoundException(TEMP_LEVEL_NOT_FOUND));
		}
		throw new InvalidWeatherRequestException(INVALID_TEMP_CONDITION);
	}

	private void validateTempCondition(final MemberTempStageOutfitRequest request) {
		if (request.extremumTmp() < 5 && request.tempCondition() == TempCondition.WARM) {
			throw new InvalidWeatherRequestException(INVALID_GROUP8_WARM_OPTION);
		}

		if (request.extremumTmp() >= 28 && request.tempCondition() == TempCondition.COOL) {
			throw new InvalidWeatherRequestException(INVALID_GROUP1_COOL_OPTION);
		}
	}

	private LinkedList<MemberOutfitGroupResponse> convertToList(
		final LinkedHashMap<Integer, List<MemberOutfit>> memberOutfitsMap) {
		return memberOutfitsMap.entrySet().stream()
			.map(entry -> MemberOutfitGroupResponse.of(
				entry.getKey(),
				entry.getValue().stream()
					.map(MemberOutfitResponse::of)
					.toList()
			))
			.collect(Collectors.toCollection(LinkedList::new));
	}

	private LinkedHashMap<Integer, List<MemberOutfit>> convertToMap(final List<MemberOutfit> memberOutfits) {
		LinkedHashMap<Integer, List<MemberOutfit>> memberOutfitsMap = new LinkedHashMap<>();
		initializeMap(memberOutfitsMap);
		for (MemberOutfit memberOutfit : memberOutfits) {
			final int level = memberOutfit.getTempStage().getLevel();
			memberOutfitsMap.get(level).add(memberOutfit);
		}
		return memberOutfitsMap;
	}

	private void initializeMap(LinkedHashMap<Integer, List<MemberOutfit>> memberOutfitsMap) {
		final List<TempStage> allTempStage = tempStageRepository.findAll();
		for (TempStage tempStage : allTempStage) {
			memberOutfitsMap.put(tempStage.getLevel(), new ArrayList<>());
		}
	}

	private TempStage getTempStageByLevel(final Integer level) {
		return tempStageRepository.findByLevel(level)
			.orElseThrow(() -> new TempStageNotFoundException(TEMP_LEVEL_NOT_FOUND));
	}

	private Member getById(final Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberNotFoundException(NOT_FOUND_MEMBER));
	}

}
