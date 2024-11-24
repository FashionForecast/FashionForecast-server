package com.example.fashionforecastbackend.outfit.presentation;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fashionforecastbackend.global.oauth2.UserDetail;
import com.example.fashionforecastbackend.global.response.ApiResponse;
import com.example.fashionforecastbackend.outfit.dto.request.OutfitRequest;
import com.example.fashionforecastbackend.outfit.dto.response.OutfitGroupResponse;
import com.example.fashionforecastbackend.outfit.service.OutfitService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/outfit")
@RequiredArgsConstructor
public class OutfitController {

	private final OutfitService outfitService;

	@PostMapping("/default")
	public ApiResponse<Void> addDefaultOutfit(@Valid @RequestBody OutfitRequest requestDto) {
		outfitService.saveOutfit(requestDto);
		return ApiResponse.ok();
	}

	@GetMapping
	public ApiResponse<OutfitGroupResponse> getOutfitGroup(@AuthenticationPrincipal UserDetail principal) {
		final Long memberId = principal.memberId();
		return ApiResponse.ok(outfitService.getOutfitGroup(memberId));
	}

}
