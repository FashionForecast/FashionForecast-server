package com.example.fashionforecastbackend.customOutfit.presentation;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fashionforecastbackend.customOutfit.dto.request.DeleteGuestOutfitRequest;
import com.example.fashionforecastbackend.customOutfit.dto.request.GuestOutfitRequest;
import com.example.fashionforecastbackend.customOutfit.dto.request.GuestTempStageOutfitRequest;
import com.example.fashionforecastbackend.customOutfit.dto.response.GuestOutfitResponse;
import com.example.fashionforecastbackend.customOutfit.service.GuestOutfitService;
import com.example.fashionforecastbackend.global.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/guest/outfit")
@RequiredArgsConstructor
public class GuestOutfitController {

	private final GuestOutfitService guestOutfitService;

	@PostMapping
	public ApiResponse<Void> addOutfit(@RequestBody @Valid final GuestOutfitRequest guestOutfitRequest) {
		guestOutfitService.saveGuestOutfit(guestOutfitRequest);
		return ApiResponse.noContent();
	}

	@GetMapping("/{uuid}")
	public ApiResponse<List<GuestOutfitResponse>> getOutfits(@PathVariable("uuid") final String uuid) {
		final List<GuestOutfitResponse> guestOutfits = guestOutfitService.getGuestOutfitsByUuid(uuid);
		return ApiResponse.ok(guestOutfits);
	}

	@GetMapping("/temp-stage")
	public ApiResponse<GuestOutfitResponse> getTempStageOutfit(
		@ModelAttribute @Valid GuestTempStageOutfitRequest request) {
		final GuestOutfitResponse guestOutfit = guestOutfitService.getGuestTempStageOutfit(request);
		return ApiResponse.ok(guestOutfit);
	}

	@DeleteMapping
	public ApiResponse<Void> deleteOutfit(@RequestBody @Valid final DeleteGuestOutfitRequest request) {
		guestOutfitService.deleteGuestOutfit(request);
		return ApiResponse.noContent();
	}

	@PatchMapping
	public ApiResponse<Void> updateOutfit(@RequestBody @Valid final GuestOutfitRequest request) {
		guestOutfitService.updateGuestOutfit(request);
		return ApiResponse.noContent();
	}
}
