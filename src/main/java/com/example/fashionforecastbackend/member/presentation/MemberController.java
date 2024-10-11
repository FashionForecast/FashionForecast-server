package com.example.fashionforecastbackend.member.presentation;

import java.util.LinkedList;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fashionforecastbackend.global.oauth2.CustomOauth2User;
import com.example.fashionforecastbackend.global.response.ApiResponse;
import com.example.fashionforecastbackend.member.dto.request.MemberGenderRequest;
import com.example.fashionforecastbackend.member.dto.request.MemberOutfitRequest;
import com.example.fashionforecastbackend.member.dto.response.MemberInfoResponse;
import com.example.fashionforecastbackend.member.dto.response.MemberOutfitGroupResponse;
import com.example.fashionforecastbackend.member.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@GetMapping
	public ApiResponse<MemberInfoResponse> getMemberInfo(@AuthenticationPrincipal CustomOauth2User principal) {
		MemberInfoResponse memberInfoResponse = memberService.getMemberInfo(principal.getMemberId());
		return ApiResponse.ok(memberInfoResponse);
	}

	@PostMapping("/gender")
	public ApiResponse<Void> addGender(@RequestBody @Valid final MemberGenderRequest memberGenderRequest,
		@AuthenticationPrincipal CustomOauth2User principal) {
		memberService.saveGender(memberGenderRequest, principal.getMemberId());
		return ApiResponse.noContent();
	}

	@PostMapping("/outfit")
	public ApiResponse<Void> addOutfit(@RequestBody @Valid final MemberOutfitRequest memberOutfitRequest,
		@AuthenticationPrincipal CustomOauth2User principal) {
		memberService.saveMemberOutfit(memberOutfitRequest, principal.getMemberId());
		return ApiResponse.noContent();
	}

	@GetMapping("/outfits")
	public ApiResponse<LinkedList<MemberOutfitGroupResponse>> getOutfits(@AuthenticationPrincipal CustomOauth2User principal) {
		final LinkedList<MemberOutfitGroupResponse> memberOutfits = memberService.getMemberOutfits(
			principal.getMemberId());
		return ApiResponse.ok(memberOutfits);
	}
}
