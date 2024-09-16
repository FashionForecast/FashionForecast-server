package com.example.fashionforecastbackend.global.login.presentation;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fashionforecastbackend.global.login.dto.response.AccessTokenResponse;
import com.example.fashionforecastbackend.global.login.service.LoginService;
import com.example.fashionforecastbackend.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/login")
@RequiredArgsConstructor
public class LoginController {

	private final LoginService loginService;

	@PostMapping("/token")
	public ApiResponse<AccessTokenResponse> issueAccessToken(@CookieValue(value = "refresh_token") final String refreshTokenRequest) {
		return ApiResponse.created(loginService.issueAccessToken(refreshTokenRequest));
	}

}
