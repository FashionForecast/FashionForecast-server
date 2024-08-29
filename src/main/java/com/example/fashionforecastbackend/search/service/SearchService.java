package com.example.fashionforecastbackend.search.service;

import java.util.List;

import com.example.fashionforecastbackend.search.dto.request.SearchRequest;
import com.example.fashionforecastbackend.search.dto.response.SearchResponse;

public interface SearchService {

	SearchResponse registSearch(String uuid, SearchRequest request);

	List<SearchResponse> getSearch(String uuid);

	void deleteSearch(String uuid, SearchRequest request);
}
