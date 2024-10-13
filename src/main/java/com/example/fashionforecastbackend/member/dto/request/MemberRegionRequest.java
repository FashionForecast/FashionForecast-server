package com.example.fashionforecastbackend.member.dto.request;

import jakarta.validation.constraints.NotNull;

public record MemberRegionRequest(
	@NotNull(message = "지역 이름을 입력해주세요.")
	String region
) {
}
