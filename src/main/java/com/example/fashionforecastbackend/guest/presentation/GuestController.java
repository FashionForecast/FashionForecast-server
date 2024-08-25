package com.example.fashionforecastbackend.guest.presentation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fashionforecastbackend.global.response.ApiResponse;
import com.example.fashionforecastbackend.guest.dto.GuestLoginRequest;
import com.example.fashionforecastbackend.guest.dto.GuestLoginResponse;
import com.example.fashionforecastbackend.guest.service.GuestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/guest")
@RequiredArgsConstructor
public class GuestController {

	private final GuestService guestService;

	@PostMapping("/login")
	public ApiResponse<GuestLoginResponse> login(@RequestBody GuestLoginRequest dto) {
		GuestLoginResponse response = guestService.login(dto);
		if (response.isNewGuest()) {
			return ApiResponse.created(response);
		}
		return ApiResponse.ok(response);
	}

}
