package com.example.fashionforecastbackend.listener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import com.example.fashionforecastbackend.member.domain.MemberDeleteEvent;
import com.example.fashionforecastbackend.member.domain.repository.MemberOutfitRepository;
import com.example.fashionforecastbackend.member.domain.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

/**
 * TODO.1 계정 정보 끊는 로직 추가
 */
@Component
@RequiredArgsConstructor
public class DeleteEventListener {

	private final MemberRepository memberRepository;
	private final MemberOutfitRepository memberOutfitRepository;


	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@TransactionalEventListener(fallbackExecution = true)
	public void deleteMemberWithOutfits (final MemberDeleteEvent event) {
		final Long memberId = event.memberId();
		memberOutfitRepository.deleteAllByMemberId(memberId);
		memberRepository.deleteById(memberId);
	}

}
