package com.example.fashionforecastbackend.outfit.service.impl;

import static com.example.fashionforecastbackend.member.domain.constant.Gender.*;
import static com.example.fashionforecastbackend.outfit.fixture.OutfitFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.fashionforecastbackend.member.domain.Member;
import com.example.fashionforecastbackend.member.domain.constant.Gender;
import com.example.fashionforecastbackend.member.domain.repository.MemberRepository;
import com.example.fashionforecastbackend.outfit.domain.Outfit;
import com.example.fashionforecastbackend.outfit.domain.repository.OutfitRepository;
import com.example.fashionforecastbackend.outfit.dto.response.OutfitGroupResponse;
import com.example.fashionforecastbackend.outfit.dto.response.OutfitResponse;
import com.example.fashionforecastbackend.tempStage.domain.repository.TempStageRepository;

@ExtendWith(MockitoExtension.class)
class OutfitServiceTest {

	@InjectMocks
	private OutfitServiceImpl outfitService;

	@Mock
	private OutfitRepository outfitRepository;

	@Mock
	private TempStageRepository tempStageRepository;

	@Mock
	private MemberRepository memberRepository;

	@Test
	@DisplayName("남자 상의, 하의 옷유형 조회")
	void male_getOutfitGroupResponseTest() throws Exception {
	    //given
		final long memberId = 1L;
		final Gender gender = MALE;
		final Member member = Member.builder()
			.id(memberId)
			.gender(gender)
			.build();
		final List<Outfit> outfits = OUTFITS;
		final List<OutfitResponse> maleTops = MALE_TOPS;
		final List<OutfitResponse> maleBottoms = MALE_BOTTOMS;

		given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
		given(outfitRepository.findByOutfitTypeIn(any(List.class))).willReturn(outfits);
		//when
		final OutfitGroupResponse outfitGroup = outfitService.getOutfitGroup(memberId);
		final List<OutfitResponse> tops = outfitGroup.tops();
		final List<OutfitResponse> bottoms = outfitGroup.bottoms();

		//then
		then(outfitRepository).should().findByOutfitTypeIn(any(List.class));

		assertAll(
			() -> assertThat(tops).isEqualTo(maleTops),
			() -> assertThat(bottoms).isEqualTo(maleBottoms)
		);
	}

	@Test
	@DisplayName("여자 상의, 하의 옷유형 조회")
	void female_getOutfitGroupResponseTest() throws Exception {
		//given
		final long memberId = 1L;
		final Gender gender = FEMALE;
		final Member member = Member.builder()
			.id(memberId)
			.gender(gender)
			.build();
		final List<Outfit> outfits = OUTFITS;
		final List<OutfitResponse> femaleTops = FEMALE_TOPS;
		final List<OutfitResponse> femaleBottoms = FEMALE_BOTTOMS;

		given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
		given(outfitRepository.findByOutfitTypeIn(any(List.class))).willReturn(outfits);
		//when
		final OutfitGroupResponse outfitGroup = outfitService.getOutfitGroup(memberId);
		final List<OutfitResponse> tops = outfitGroup.tops();
		final List<OutfitResponse> bottoms = outfitGroup.bottoms();

		//then
		then(outfitRepository).should().findByOutfitTypeIn(any(List.class));

		assertAll(
			() -> assertThat(tops).isEqualTo(femaleTops),
			() -> assertThat(bottoms).isEqualTo(femaleBottoms)
		);
	}

}