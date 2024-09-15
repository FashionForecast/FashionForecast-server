package com.example.fashionforecastbackend.global.login.presentation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fashionforecastbackend.global.login.dto.request.AccessTokenRequest;
import com.example.fashionforecastbackend.global.login.dto.response.AccessTokenResponse;
import com.example.fashionforecastbackend.global.login.service.LoginService;
import com.example.fashionforecastbackend.global.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/login")
@RequiredArgsConstructor
public class LoginController {

	private final LoginService loginService;

	@PostMapping("/token")
	public ApiResponse<AccessTokenResponse> issueAccessToken(@Valid @RequestBody AccessTokenRequest request) {
		return ApiResponse.created(loginService.issueAccessToken(request));
	}

}
