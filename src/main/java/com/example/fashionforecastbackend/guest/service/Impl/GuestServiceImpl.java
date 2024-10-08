package com.example.fashionforecastbackend.guest.service.Impl;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fashionforecastbackend.guest.domain.Guest;
import com.example.fashionforecastbackend.guest.domain.repository.GuestRepository;
import com.example.fashionforecastbackend.guest.dto.GuestLoginRequest;
import com.example.fashionforecastbackend.guest.dto.GuestLoginResponse;
import com.example.fashionforecastbackend.guest.service.GuestService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GuestServiceImpl implements GuestService {

	private final GuestRepository guestRepository;

	@Override
	public GuestLoginResponse login(final GuestLoginRequest dto) {
		return findOrCreateGuest(dto.uuid());
	}

	private GuestLoginResponse findOrCreateGuest(final String uuid) {
		boolean isExist = guestRepository.existsByUuid(uuid);
		Guest guest = guestRepository.findByUuid(uuid).orElseGet(this::createGuest);
		return GuestLoginResponse.of(guest.getUuid(), !isExist);
	}

	private Guest createGuest() {
		return guestRepository.save(
			Guest.builder()
				.uuid(UUID.randomUUID().toString())
				.build()
		);
	}
}
