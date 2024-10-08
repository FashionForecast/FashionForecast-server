package com.example.fashionforecastbackend.member.dto.request;

import com.example.fashionforecastbackend.member.domain.Gender;

import jakarta.validation.constraints.NotNull;

public record MemberGenderRequest(
	@NotNull(message = "성별 입력은 필수입니다.")
	Gender gender
) {
}
