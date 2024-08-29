package com.example.fashionforecastbackend.search.util;

import org.springframework.stereotype.Component;

import com.example.fashionforecastbackend.global.error.ErrorCode;
import com.example.fashionforecastbackend.global.error.exception.SearchNotExistException;
import com.example.fashionforecastbackend.guest.domain.repository.GuestRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SearchValidator {

	private final GuestRepository guestRepository;

	public void validateExistUser(final String uuid) {
		if (!isExistUser(uuid)) {
			throw new SearchNotExistException(ErrorCode.SEARCH_NOT_EXIST_USER);
		}
	}

	/*
	Member 존재 검증도 추가 예정
	 */
	private boolean isExistUser(final String uuid) {
		return guestRepository.existsByUuid(uuid);
	}

}
