package com.example.fashionforecastbackend.customOutfit.dto.request;

import jakarta.validation.constraints.NotNull;

public record GuestOutfitsRequest(
	@NotNull(message = "uuid는 필수 입니다.")
	String uuid
) {
}
