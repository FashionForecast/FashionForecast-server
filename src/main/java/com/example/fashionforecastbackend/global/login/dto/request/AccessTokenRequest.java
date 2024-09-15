package com.example.fashionforecastbackend.global.login.dto.request;

import jakarta.validation.constraints.NotNull;

public record AccessTokenRequest(

	@NotNull(message = "memberId를 입력해주세요")
	Long memberId
) {

}
