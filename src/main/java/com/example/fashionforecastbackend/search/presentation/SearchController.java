package com.example.fashionforecastbackend.search.presentation;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fashionforecastbackend.global.response.ApiResponse;
import com.example.fashionforecastbackend.search.dto.request.SearchRequest;
import com.example.fashionforecastbackend.search.dto.response.SearchResponse;
import com.example.fashionforecastbackend.search.service.SearchService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

	private final SearchService searchService;


	@PostMapping("/{uuid}")
	public ApiResponse<SearchResponse> registSearch(
		@PathVariable final String uuid,
		@RequestBody @Valid final SearchRequest request) {
		return ApiResponse.created(searchService.registSearch(uuid, request));
	}

	@GetMapping("/{uuid}")
	public ApiResponse<List<SearchResponse>> getSearches(
		@PathVariable final String uuid
	) {
		return ApiResponse.ok(searchService.getSearch(uuid));
	}

	@DeleteMapping("/{uuid}")
	public ApiResponse<Void> deleteSearch(
		@PathVariable final String uuid,
		@RequestBody @Valid final SearchRequest request
	) {
		searchService.deleteSearch(uuid, request);
		return ApiResponse.noContent();

	}
}
