package com.example.fashionforecastbackend.search;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.example.fashionforecastbackend.global.error.ErrorCode;
import com.example.fashionforecastbackend.global.error.exception.SearchNotExistException;
import com.example.fashionforecastbackend.guest.domain.repository.GuestRepository;
import com.example.fashionforecastbackend.member.domain.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class UuidChecker {

	private final GuestRepository guestRepository;
	private final MemberRepository memberRepository;

	@Before("@within(ValidateUuid)")
	public void validateUuid(final JoinPoint joinPoint) {
		Arrays.stream(joinPoint.getArgs())
			.filter(String.class::isInstance)
			.map(String.class::cast)
			.filter(this::hasUuid)
			.findAny()
			.orElseThrow(() -> new SearchNotExistException(ErrorCode.SEARCH_NOT_EXIST_USER));

	}

	private boolean hasUuid(final String uuid) {
		return guestRepository.existsByUuid(uuid) || memberRepository.existsBySocialId(uuid);
	}
}
