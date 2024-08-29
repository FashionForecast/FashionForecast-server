package com.example.fashionforecastbackend.search.dto.request;

import jakarta.validation.constraints.NotNull;

public record SearchRequest(

	@NotNull(message = "City를 입력해주세요")
	String city,
	@NotNull(message = "District를 입력해주세요")
	String district

) {

}
