package com.example.fashionforecastbackend.member.presentation;

import java.util.LinkedList;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fashionforecastbackend.global.oauth2.UserDetail;
import com.example.fashionforecastbackend.global.response.ApiResponse;
import com.example.fashionforecastbackend.member.dto.request.MemberOutfitRequest;
import com.example.fashionforecastbackend.member.dto.request.MemberTempStageOutfitRequest;
import com.example.fashionforecastbackend.member.dto.response.MemberOutfitGroupResponse;
import com.example.fashionforecastbackend.member.dto.response.MemberOutfitResponse;
import com.example.fashionforecastbackend.member.service.MemberOutfitService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberOutfitController {

	private final MemberOutfitService memberOutfitService;

	@PostMapping("/outfit")
	public ApiResponse<Void> addOutfit(@RequestBody @Valid final MemberOutfitRequest memberOutfitRequest,
		@AuthenticationPrincipal UserDetail principal) {
		memberOutfitService.saveMemberOutfit(memberOutfitRequest, principal.memberId());
		return ApiResponse.noContent();
	}

	@GetMapping("/outfits")
	public ApiResponse<LinkedList<MemberOutfitGroupResponse>> getOutfits(@AuthenticationPrincipal final UserDetail principal) {
		final LinkedList<MemberOutfitGroupResponse> memberOutfits = memberOutfitService.getMemberOutfits(
			principal.memberId());
		return ApiResponse.ok(memberOutfits);
	}

	@GetMapping("/outfits/temp-stage")
	public ApiResponse<List<MemberOutfitResponse>> getTempStageOutfits(
		@AuthenticationPrincipal final UserDetail principal,
		@ModelAttribute @Valid MemberTempStageOutfitRequest request) {
		final List<MemberOutfitResponse> memberTempStageOutfits = memberOutfitService.getMemberTempStageOutfits(request,
			principal.memberId());
		return ApiResponse.ok(memberTempStageOutfits);
	}

	@DeleteMapping("/outfits/{memberOutfitId}")
	public ApiResponse<Void> deleteOutfit(@PathVariable final Long memberOutfitId) {
		memberOutfitService.deleteMemberOutfit(memberOutfitId);
		return ApiResponse.noContent();
	}

	@PatchMapping("/outfits/{memberOutfitId}")
	public ApiResponse<Void> updateOutfit(
		@PathVariable final Long memberOutfitId,
		@RequestBody final MemberOutfitRequest request) {
		memberOutfitService.updateMemberOutfit(memberOutfitId, request);
		return ApiResponse.noContent();
	}
}
