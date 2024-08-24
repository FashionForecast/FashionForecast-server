package com.example.fashionforecastbackend.guest.service;

import com.example.fashionforecastbackend.guest.dto.GuestLoginRequest;
import com.example.fashionforecastbackend.guest.dto.GuestLoginResponse;

public interface GuestService {

	GuestLoginResponse login(GuestLoginRequest dto);

}
