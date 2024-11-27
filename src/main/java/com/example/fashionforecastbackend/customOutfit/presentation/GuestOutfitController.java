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
@RequestMapping("/api/v1/guest")
@RequiredArgsConstructor
public class GuestOutfitController {

	private final GuestOutfitService guestOutfitService;

	@PostMapping("/outfit")
	public ApiResponse<Void> addOutfit(@RequestBody @Valid final GuestOutfitRequest guestOutfitRequest) {
		guestOutfitService.saveGuestOutfit(guestOutfitRequest);
		return ApiResponse.noContent();
	}

	@GetMapping("/outfits/{uuid}")
	public ApiResponse<List<GuestOutfitResponse>> getOutfits(@PathVariable("uuid") final String uuid) {
		final List<GuestOutfitResponse> guestOutfits = guestOutfitService.getGuestOutfitsByUuid(uuid);
		return ApiResponse.ok(guestOutfits);
	}

	@GetMapping("/outfits/temp-stage/{uuid}")
	public ApiResponse<GuestOutfitResponse> getTempStageOutfits(
		@ModelAttribute @Valid GuestTempStageOutfitRequest request,
		@PathVariable("uuid") final String uuid) {
		final GuestOutfitResponse guestOutfit = guestOutfitService.getGuestTempStageOutfits(request, uuid);
		return ApiResponse.ok(guestOutfit);
	}

	@DeleteMapping("/outfits")
	public ApiResponse<Void> deleteOutfit(@RequestBody @Valid final DeleteGuestOutfitRequest request) {
		guestOutfitService.deleteGuestOutfit(request);
		return ApiResponse.noContent();
	}

	@PatchMapping("/outfits")
	public ApiResponse<Void> updateOutfit(@RequestBody @Valid final GuestOutfitRequest request) {
		guestOutfitService.updateGuestOutfit(request);
		return ApiResponse.noContent();
	}
}