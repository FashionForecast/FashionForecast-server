package com.example.fashionforecastbackend.search.dto.response;

import com.example.fashionforecastbackend.search.domain.Search;

public record SearchResponse(

	String city,
	String district

) {

	public static SearchResponse from(final Search search) {
		return new SearchResponse(search.getCity(), search.getDistrict());

	}

}
