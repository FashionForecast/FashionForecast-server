package com.example.fashionforecastbackend.listener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import com.example.fashionforecastbackend.global.error.ErrorCode;
import com.example.fashionforecastbackend.global.error.exception.MemberNotFoundException;
import com.example.fashionforecastbackend.member.domain.Member;
import com.example.fashionforecastbackend.member.domain.MemberDeleteEvent;
import com.example.fashionforecastbackend.member.domain.repository.MemberOutfitRepository;
import com.example.fashionforecastbackend.member.domain.repository.MemberRepository;
import com.example.fashionforecastbackend.search.service.SearchService;

import lombok.RequiredArgsConstructor;

/**
 * TODO.1 계정 정보 끊는 로직 추가
 */
@Component
@RequiredArgsConstructor
public class DeleteEventListener {

	private final MemberRepository memberRepository;
	private final MemberOutfitRepository memberOutfitRepository;
	private final SearchService searchService;


	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@TransactionalEventListener(fallbackExecution = true)
	public void deleteMemberWithOutfits (final MemberDeleteEvent event) {
		final Long memberId = event.memberId();
		memberOutfitRepository.deleteAllByMemberId(memberId);
		memberRepository.deleteById(memberId);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@TransactionalEventListener(fallbackExecution = true)
	public void deleteAllSearch(final MemberDeleteEvent event) {
		final Long memberId = event.memberId();
		final Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberNotFoundException(ErrorCode.NOT_FOUND_MEMBER));
		searchService.deleteAllSearch(member.getSocialId());
	}

}
