package com.example.fashionforecastbackend.customOutfit.service.impl;

import static com.example.fashionforecastbackend.global.error.ErrorCode.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fashionforecastbackend.customOutfit.domain.MemberOutfit;
import com.example.fashionforecastbackend.customOutfit.domain.constant.BottomAttribute;
import com.example.fashionforecastbackend.customOutfit.domain.constant.TopAttribute;
import com.example.fashionforecastbackend.customOutfit.domain.repository.MemberOutfitRepository;
import com.example.fashionforecastbackend.customOutfit.dto.request.GuestOutfitsRequest;
import com.example.fashionforecastbackend.customOutfit.dto.request.MemberOutfitRequest;
import com.example.fashionforecastbackend.customOutfit.dto.request.MemberTempStageOutfitRequest;
import com.example.fashionforecastbackend.customOutfit.dto.response.GuestOutfitResponse;
import com.example.fashionforecastbackend.customOutfit.dto.response.MemberOutfitGroupResponse;
import com.example.fashionforecastbackend.customOutfit.dto.response.MemberOutfitResponse;
import com.example.fashionforecastbackend.customOutfit.service.GuestOutfitService;
import com.example.fashionforecastbackend.customOutfit.service.MemberOutfitService;
import com.example.fashionforecastbackend.global.error.exception.AlreadyExistException;
import com.example.fashionforecastbackend.global.error.exception.MemberNotFoundException;
import com.example.fashionforecastbackend.global.error.exception.MemberOutfitLimitExceededException;
import com.example.fashionforecastbackend.global.error.exception.MemberOutfitNotFoundException;
import com.example.fashionforecastbackend.guest.service.GuestService;
import com.example.fashionforecastbackend.member.domain.Member;
import com.example.fashionforecastbackend.member.domain.repository.MemberRepository;
import com.example.fashionforecastbackend.tempStage.domain.TempStage;
import com.example.fashionforecastbackend.tempStage.service.TempStageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberOutfitServiceImpl implements MemberOutfitService {

	private final MemberRepository memberRepository;
	private final MemberOutfitRepository memberOutfitRepository;
	private final TempStageService tempStageService;
	private final GuestOutfitService guestOutfitService;
	private final GuestService guestService;

	@Transactional
	@Override
	public void saveMemberOutfit(final MemberOutfitRequest memberOutfitRequest, final Long memberId) {
		final Member member = getMemberById(memberId);
		final TempStage tempStage = tempStageService.getTempStageByLevel(memberOutfitRequest.tempStageLevel());
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
		final Member member = getMemberById(memberId);
		final TempStage tempStage = tempStageService.getTempStageByWeather(request.extremumTmp(),
			request.tempCondition());
		final List<MemberOutfit> outfits = memberOutfitRepository.findByMemberIdAndTempStageId(member.getId(),
			tempStage.getId());
		return outfits.stream()
			.map(MemberOutfitResponse::of)
			.toList();
	}

	@Transactional
	@Override
	public void updateMemberOutfit(final Long memberOutfitId, final MemberOutfitRequest request) {
		final MemberOutfit memberOutfit = getMemberOutfitById(memberOutfitId);
		final TopAttribute topAttribute = TopAttribute.of(request.topType(), request.topColor());
		final BottomAttribute bottomAttribute = BottomAttribute.of(request.bottomType(), request.bottomColor());
		memberOutfit.updateTopAttribute(topAttribute);
		memberOutfit.updateBottomAttribute(bottomAttribute);
	}

	@Transactional
	@Override
	public void saveMemberOutfitFromGuestOutfit(final GuestOutfitsRequest guestOutfitsRequest, final Long memberId) {
		String uuid = guestOutfitsRequest.uuid();
		validateMemberOutfitCount(memberId);

		List<MemberOutfit> memberOutfits = createMemberOutfitByGuestOutfit(uuid, memberId);

		memberOutfitRepository.saveAll(memberOutfits);

		guestOutfitService.deleteAllGuestOutfit(uuid);

		guestService.deleteGuest(uuid);
	}

	private void validateCount(final Long tempStageId, final Long memberId) {
		final Integer count = memberOutfitRepository.countByTempStageIdAndMemberId(tempStageId, memberId);
		if (count >= 4) {
			throw new MemberOutfitLimitExceededException(MEMBER_OUTFIT_COUNT_EXCEEDED);
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
		final List<TempStage> allTempStage = tempStageService.findAllTempStage();
		for (TempStage tempStage : allTempStage) {
			memberOutfitsMap.put(tempStage.getLevel(), new ArrayList<>());
		}
	}

	private void validateMemberOutfitCount(final Long memberId) {
		Member member = getMemberById(memberId);
		if(!member.notExistOutfit()) {
			throw new AlreadyExistException(ALREADY_EXIST_MEMBER_OUTFIT);
		}
	}

	private List<MemberOutfit> createMemberOutfitByGuestOutfit(String uuid, Long memberId) {
		List<GuestOutfitResponse> guestOutfits = guestOutfitService.getGuestOutfitsByUuid(uuid);
		Member member = getMemberById(memberId);

		return guestOutfits.stream()
			.map(guestOutfit -> {
				MemberOutfit memberOutfit = MemberOutfit.builder()
					.topAttribute(TopAttribute.of(guestOutfit.topType(), guestOutfit.topColor()))
					.bottomAttribute(
						BottomAttribute.of(guestOutfit.bottomType(), guestOutfit.bottomColor()))
					.tempStage(tempStageService.getTempStageByLevel(guestOutfit.tempStageLevel()))
					.build();
				memberOutfit.setMember(member);
				return memberOutfit;
			})
			.toList();
	}

	private Member getMemberById(final Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberNotFoundException(NOT_FOUND_MEMBER));
	}

	private MemberOutfit getMemberOutfitById(final Long memberOutfitId) {
		return memberOutfitRepository.findById(memberOutfitId)
			.orElseThrow(() -> new MemberOutfitNotFoundException(NOT_FOUND_MEMBER_OUTFIT));
	}

}
