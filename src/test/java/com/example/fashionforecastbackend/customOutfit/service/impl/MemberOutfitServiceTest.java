package com.example.fashionforecastbackend.customOutfit.service.impl;

import static com.example.fashionforecastbackend.customOutfit.fixture.MemberOutfitFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.fashionforecastbackend.customOutfit.domain.MemberOutfit;
import com.example.fashionforecastbackend.customOutfit.domain.constant.BottomAttribute;
import com.example.fashionforecastbackend.customOutfit.domain.constant.TopAttribute;
import com.example.fashionforecastbackend.customOutfit.domain.repository.MemberOutfitRepository;
import com.example.fashionforecastbackend.customOutfit.dto.request.MemberOutfitRequest;
import com.example.fashionforecastbackend.customOutfit.dto.request.MemberTempStageOutfitRequest;
import com.example.fashionforecastbackend.customOutfit.dto.response.MemberOutfitGroupResponse;
import com.example.fashionforecastbackend.customOutfit.dto.response.MemberOutfitResponse;
import com.example.fashionforecastbackend.member.domain.Member;
import com.example.fashionforecastbackend.member.domain.repository.MemberRepository;
import com.example.fashionforecastbackend.recommend.domain.TempCondition;
import com.example.fashionforecastbackend.tempStage.domain.TempStage;
import com.example.fashionforecastbackend.tempStage.fixture.TempStageFixture;
import com.example.fashionforecastbackend.tempStage.service.TempStageService;

@ExtendWith(MockitoExtension.class)
class MemberOutfitServiceTest {

	@InjectMocks
	private MemberOutfitServiceImpl memberOutfitService;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private TempStageService tempStageService;

	@Mock
	private MemberOutfitRepository memberOutfitRepository;

	@DisplayName("멤버 옷차림 저장 성공")
	@Test
	void saveMemberOutfitTest() {
		//given
		final long memberId = 1L;
		final Member member = Member.builder()
			.id(memberId)
			.build();

		final int tempStageLevel = 2;
		final int minTemp = 23;
		final int maxTemp = 27;
		final TempStage tempStage = TempStage.create(tempStageLevel, minTemp, maxTemp);

		final MemberOutfitRequest memberOutfitRequest = new MemberOutfitRequest("반팔티", "#111111", "슬랙스", "#222222",
			tempStageLevel);

		given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
		given(tempStageService.getTempStageByLevel(tempStageLevel)).willReturn(tempStage);

		//when
		memberOutfitService.saveMemberOutfit(memberOutfitRequest, memberId);

		//then
		then(memberRepository).should().findById(memberId);
		then(memberOutfitRepository).should().save(any(MemberOutfit.class));

		MemberOutfit savedOutfit = member.getMemberOutfits().get(0);

		assertAll(
			() -> assertThat(savedOutfit.getTopAttribute().getTopType()).isEqualTo(memberOutfitRequest.topType()),
			() -> assertThat(savedOutfit.getTopAttribute().getTopColor()).isEqualTo(memberOutfitRequest.topColor()),
			() -> assertThat(savedOutfit.getBottomAttribute().getBottomType()).isEqualTo(
				memberOutfitRequest.bottomType()),
			() -> assertThat(savedOutfit.getBottomAttribute().getBottomColor()).isEqualTo(
				memberOutfitRequest.bottomColor()),
			() -> assertThat(savedOutfit.getTempStage().getLevel()).isEqualTo(memberOutfitRequest.tempStageLevel())
		);
	}

	@Test
	@DisplayName("멤버 옷차림 조회 성공")
	void getMemberOutfitsTest() throws Exception {
		//given
		final long memberId = 1L;
		final LinkedList<MemberOutfit> memberOutfits = new LinkedList<>(MEMBER_OUTFITS);
		final List<TempStage> tempStages = TempStageFixture.TEMP_STAGE_ALL;
		given(memberOutfitRepository.findByMemberIdWithTempStage(memberId)).willReturn(memberOutfits);
		given(tempStageService.findAllTempStage()).willReturn(tempStages);
		//when
		final LinkedList<MemberOutfitGroupResponse> memberOutfitsGroups = memberOutfitService.getMemberOutfits(
			memberId);

		//then
		then(memberOutfitRepository).should().findByMemberIdWithTempStage(memberId);

		assertAll(
			() -> assertThat(memberOutfitsGroups.size()).isEqualTo(tempStages.size()),
			() -> assertThat(memberOutfitsGroups.get(2).tempStageLevel()).isEqualTo(tempStages.get(2).getLevel()),
			() -> assertThat(memberOutfitsGroups.get(3).tempStageLevel()).isEqualTo(tempStages.get(3).getLevel())
		);
	}

	@Test
	@DisplayName("멤버 온도에 따른 옷차림 조회 성공")
	void getTempStageOutfitTest() throws Exception {
		//given
		final long memberId = 1L;
		final Member member = Member.builder().id(memberId).build();
		final MemberTempStageOutfitRequest request = new MemberTempStageOutfitRequest(20, TempCondition.NORMAL);
		final TempStage tempStage = TempStage.create(2, 20, 22);
		final List<MemberOutfit> memberTempStageOutfits = MEMBER_TEMP_STAGE_OUTFITS;
		given(memberRepository.findById(any(Long.class))).willReturn(Optional.of(member));
		given(tempStageService.getTempStageByWeather(request.extremumTmp(), request.tempCondition())).willReturn(
			tempStage);
		given(memberOutfitRepository.findByMemberIdAndTempStageId(memberId, tempStage.getId())).willReturn(
			memberTempStageOutfits);

		//when
		final List<MemberOutfitResponse> outfits = memberOutfitService.getMemberTempStageOutfits(request, memberId);

		//then
		then(memberRepository).should().findById(memberId);
		then(tempStageService).should().getTempStageByWeather(request.extremumTmp(), request.tempCondition());
		then(memberOutfitRepository).should().findByMemberIdAndTempStageId(memberId, tempStage.getId());

		assertThat(outfits.size()).isEqualTo(memberTempStageOutfits.size());

	}

	@Test
	@DisplayName("멤버 옷차림 수정 성공")
	void updateMemberOutfitTest() throws Exception {
		//given
		final Long memberOutfitId = 1L;
		final MemberOutfit memberOutfit = MemberOutfit.builder()
			.id(memberOutfitId)
			.topAttribute(TopAttribute.of("반팔티", "#111111"))
			.bottomAttribute(BottomAttribute.of("반바지", "222222"))
			.build();
		final MemberOutfitRequest memberOutfitRequest = MEMBER_OUTFIT_REQUEST;

		given(memberOutfitRepository.findById(memberOutfitId)).willReturn(Optional.of(memberOutfit));
		//when
		memberOutfitService.updateMemberOutfit(memberOutfitId, memberOutfitRequest);
		final TopAttribute topAttribute = memberOutfit.getTopAttribute();
		final BottomAttribute bottomAttribute = memberOutfit.getBottomAttribute();

		//then
		then(memberOutfitRepository).should().findById(memberOutfitId);

		assertAll(
			() -> assertThat(topAttribute.getTopType()).isEqualTo(memberOutfitRequest.topType()),
			() -> assertThat(topAttribute.getTopColor()).isEqualTo(memberOutfitRequest.topColor()),
			() -> assertThat(bottomAttribute.getBottomType()).isEqualTo(memberOutfitRequest.bottomType()),
			() -> assertThat(bottomAttribute.getBottomColor()).isEqualTo(memberOutfitRequest.bottomColor())
		);

	}
}
