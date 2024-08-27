package com.example.fashionforecastbackend.outfit.presentation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fashionforecastbackend.global.response.ApiResponse;
import com.example.fashionforecastbackend.outfit.dto.OutfitRequest;
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
}
