package com.example.fashionforecastbackend.search.service;

import java.util.List;

import com.example.fashionforecastbackend.search.dto.request.SearchRequest;
import com.example.fashionforecastbackend.search.dto.response.SearchResponse;

public interface SearchService {

	SearchResponse registSearch(final String uuid, final SearchRequest request);

	List<SearchResponse> getSearch(final String uuid);

	void deleteSearch(final String uuid, final SearchRequest request);

	void deleteAllSearch(final String uuid);
}
