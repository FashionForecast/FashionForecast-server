package com.example.fashionforecastbackend.member.presentation;

import java.util.LinkedList;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fashionforecastbackend.global.oauth2.UserDetail;
import com.example.fashionforecastbackend.global.response.ApiResponse;
import com.example.fashionforecastbackend.member.dto.request.MemberGenderRequest;
import com.example.fashionforecastbackend.member.dto.request.MemberOutfitRequest;
import com.example.fashionforecastbackend.member.dto.request.MemberTempStageOutfitRequest;
import com.example.fashionforecastbackend.member.dto.response.MemberInfoResponse;
import com.example.fashionforecastbackend.member.dto.response.MemberOutfitGroupResponse;
import com.example.fashionforecastbackend.member.dto.response.MemberOutfitResponse;
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

	@PostMapping("/outfit")
	public ApiResponse<Void> addOutfit(@RequestBody @Valid final MemberOutfitRequest memberOutfitRequest,
		@AuthenticationPrincipal UserDetail principal) {
		memberService.saveMemberOutfit(memberOutfitRequest, principal.memberId());
		return ApiResponse.noContent();
	}

	@GetMapping("/outfits")
	public ApiResponse<LinkedList<MemberOutfitGroupResponse>> getOutfits(@AuthenticationPrincipal final UserDetail principal) {
		final LinkedList<MemberOutfitGroupResponse> memberOutfits = memberService.getMemberOutfits(
			principal.memberId());
		return ApiResponse.ok(memberOutfits);
	}

	@GetMapping("/outfits/temp-stage")
	public ApiResponse<List<MemberOutfitResponse>> getTempStageOutfits(
		@AuthenticationPrincipal final UserDetail principal,
		@ModelAttribute @Valid MemberTempStageOutfitRequest request) {
		final List<MemberOutfitResponse> memberTempStageOutfits = memberService.getMemberTempStageOutfits(request,
			principal.memberId());
		return ApiResponse.ok(memberTempStageOutfits);
	}

	@DeleteMapping("/outfits/{memberOutfitId}")
	public ApiResponse<Void> deleteOutfit(@PathVariable final Long memberOutfitId) {
		memberService.deleteMemberOutfit(memberOutfitId);
		return ApiResponse.noContent();
	}
}
