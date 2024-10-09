package com.example.fashionforecastbackend.member.service.impl;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.fashionforecastbackend.member.domain.Member;
import com.example.fashionforecastbackend.member.domain.MemberOutfit;
import com.example.fashionforecastbackend.member.domain.repository.MemberOutfitRepository;
import com.example.fashionforecastbackend.member.domain.repository.MemberRepository;
import com.example.fashionforecastbackend.member.dto.request.MemberOutfitRequest;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

	@InjectMocks
	private MemberServiceImpl memberService;

	@Mock
	private MemberRepository memberRepository;

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

		final MemberOutfitRequest memberOutfitRequest = new MemberOutfitRequest("반팔티", "#111111", "슬랙스", "#222222");

		given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

		//when
		memberService.saveMemberOutfit(memberOutfitRequest, memberId);

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
				memberOutfitRequest.bottomColor())
		);
	}
}