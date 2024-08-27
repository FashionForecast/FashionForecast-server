package com.example.fashionforecastbackend.recommend.presetation;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fashionforecastbackend.global.response.ApiResponse;
import com.example.fashionforecastbackend.recommend.dto.RecommendRequest;
import com.example.fashionforecastbackend.recommend.dto.RecommendResponse;
import com.example.fashionforecastbackend.recommend.service.RecommendService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/recommend")
@RequiredArgsConstructor
public class RecommendationController {

	private final RecommendService recommendService;

	@GetMapping("/default")
	public ApiResponse<List<RecommendResponse>> getDefaultRecommend(@Valid RecommendRequest requestDto) {
		return ApiResponse.ok(recommendService.getRecommendedOutfit(requestDto));
	}
}
