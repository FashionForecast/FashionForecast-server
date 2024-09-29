package com.example.fashionforecastbackend.board.dto.request;

import jakarta.validation.constraints.NotNull;

public record BoardRequest(
	@NotNull(message = "비어있을 수 없습니다.")
	String text
) {
}
