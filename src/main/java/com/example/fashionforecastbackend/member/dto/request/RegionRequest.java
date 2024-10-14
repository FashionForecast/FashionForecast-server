package com.example.fashionforecastbackend.member.dto.request;

import jakarta.validation.constraints.NotNull;

public record RegionRequest(
	@NotNull(message = "지역을 입력해주세요.")
	String region
) {
}
