package com.example.fashionforecastbackend.member.presentation;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fashionforecastbackend.global.oauth2.UserDetail;
import com.example.fashionforecastbackend.global.response.ApiResponse;
import com.example.fashionforecastbackend.member.dto.request.MemberGenderRequest;
import com.example.fashionforecastbackend.member.dto.request.OutingTimeRequest;
import com.example.fashionforecastbackend.member.dto.request.RegionRequest;
import com.example.fashionforecastbackend.member.dto.request.TempConditionRequest;
import com.example.fashionforecastbackend.member.dto.response.MemberInfoResponse;
import com.example.fashionforecastbackend.member.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@GetMapping
	public ApiResponse<MemberInfoResponse> getMemberInfo(@AuthenticationPrincipal UserDetail principal) {
		MemberInfoResponse memberInfoResponse = memberService.getMemberInfo(principal.memberId());
		return ApiResponse.ok(memberInfoResponse);
	}

	@PostMapping("/gender")
	public ApiResponse<Void> addGender(@RequestBody @Valid final MemberGenderRequest memberGenderRequest,
		@AuthenticationPrincipal UserDetail principal) {
		memberService.saveGender(memberGenderRequest, principal.memberId());
		return ApiResponse.noContent();
	}

	@PatchMapping("/region")
	public ApiResponse<Void> updateRegion(@RequestBody @Valid RegionRequest regionRequest,
		@AuthenticationPrincipal UserDetail principal) {
		memberService.updateRegion(regionRequest, principal.memberId());
		return ApiResponse.noContent();
	}

	@PatchMapping("/outingTime")
	public ApiResponse<Void> updateOutingTime(@RequestBody @Valid OutingTimeRequest outingTimeRequest,
		@AuthenticationPrincipal UserDetail principal) {
		memberService.updateOutingTime(outingTimeRequest, principal.memberId());
		return ApiResponse.noContent();
	}

	@PatchMapping("/temp-condition")
	public ApiResponse<Void> updateTempCondition(@RequestBody @Valid TempConditionRequest tempConditionRequest,
		@AuthenticationPrincipal UserDetail principal) {
		memberService.updateTempStage(tempConditionRequest, principal.memberId());
		return ApiResponse.noContent();
	}
}