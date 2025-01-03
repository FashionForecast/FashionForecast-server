package com.example.fashionforecastbackend.guest.service;

import com.example.fashionforecastbackend.guest.domain.Guest;
import com.example.fashionforecastbackend.guest.dto.GuestLoginRequest;
import com.example.fashionforecastbackend.guest.dto.GuestLoginResponse;

public interface GuestService {

	GuestLoginResponse login(GuestLoginRequest dto);

	Guest getGuestByUuid(String uuid);

	void deleteGuest(String uuid);

}
