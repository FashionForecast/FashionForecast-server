package com.example.fashionforecastbackend.member.presentation;

import java.util.LinkedList;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fashionforecastbackend.global.oauth2.UserDetail;
import com.example.fashionforecastbackend.global.response.ApiResponse;
import com.example.fashionforecastbackend.member.dto.request.MemberGenderRequest;
import com.example.fashionforecastbackend.member.dto.request.MemberOutfitRequest;
import com.example.fashionforecastbackend.member.dto.request.MemberRegionRequest;
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

	@PutMapping("/region")
	public ApiResponse<Void> addRegion(@RequestBody @Valid MemberRegionRequest memberRegionRequest,
		@AuthenticationPrincipal UserDetail principal) {
		memberService.updateRegion(memberRegionRequest, principal.memberId());
		return ApiResponse.noContent();
	}

	@DeleteMapping("/region")
	public ApiResponse<Void> deleteRegion(@AuthenticationPrincipal UserDetail principal) {
		memberService.updateRegion(null, principal.memberId());
		return ApiResponse.noContent();
	}

}
