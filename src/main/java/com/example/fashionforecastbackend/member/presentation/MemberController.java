package com.example.fashionforecastbackend.member.presentation;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fashionforecastbackend.global.oauth2.CustomOauth2User;
import com.example.fashionforecastbackend.global.response.ApiResponse;
import com.example.fashionforecastbackend.member.dto.request.MemberGenderRequest;
import com.example.fashionforecastbackend.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/gender")
	public ApiResponse<Void> addGender(@RequestBody MemberGenderRequest memberGenderRequest,
		@AuthenticationPrincipal CustomOauth2User principal) {
		memberService.saveGender(memberGenderRequest, principal.getMemberId());
		return ApiResponse.noContent();
	}

}
