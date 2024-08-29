package com.example.fashionforecastbackend.search.fixture;

import java.util.List;

import com.example.fashionforecastbackend.search.domain.Search;
import com.example.fashionforecastbackend.search.dto.request.SearchRequest;
import com.example.fashionforecastbackend.search.dto.response.SearchResponse;

public class SearchFixture {

	public final static String UUID = "f6c4a27e-0524-4952-8080-3498b279d724";
	public final static List<SearchResponse> SEARCH_RESPONSES = List.of(
		SearchResponse.from(Search.builder().city("서울특별시").district("관악구").build()),
		SearchResponse.from(Search.builder().city("경기도").district("남양주시").build()),
		SearchResponse.from(Search.builder().city("서울특별시").district("서초구").build())
	);
	public final static SearchRequest SEARCH_REQUEST = new SearchRequest("서울특별시", "관악구");
	public final static SearchResponse SEARCH_RESPONSE = SearchResponse.from(
		Search.builder().city("서울특별시").district("관악구").build());

}
