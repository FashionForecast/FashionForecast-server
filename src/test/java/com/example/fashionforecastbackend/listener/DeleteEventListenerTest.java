package com.example.fashionforecastbackend.listener;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.fashionforecastbackend.member.domain.MemberDeleteEvent;
import com.example.fashionforecastbackend.member.domain.repository.MemberOutfitRepository;
import com.example.fashionforecastbackend.member.domain.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class DeleteEventListenerTest {
	
	@Mock
	private MemberRepository memberRepository;
	
	@Mock
	private MemberOutfitRepository memberOutfitRepository;
	
	@InjectMocks
	private DeleteEventListener deleteEventListener;
	
	@Test
	@DisplayName("deleteMemberWithOutfits 레파지토리 호출 성공")
	void deleteMemberWithOutfitsTest() throws Exception {
	    //given
		final MemberDeleteEvent memberDeleteEvent = MemberDeleteEvent.of(1L);
		willDoNothing().given(memberOutfitRepository).deleteAllByMemberId(memberDeleteEvent.memberId());
		willDoNothing().given(memberRepository).deleteById(memberDeleteEvent.memberId());
		//when
		deleteEventListener.deleteMemberWithOutfits(memberDeleteEvent);
	    //then
		then(memberOutfitRepository).should().deleteAllByMemberId(memberDeleteEvent.memberId());
		then(memberRepository).should().deleteById(memberDeleteEvent.memberId());
	}

}