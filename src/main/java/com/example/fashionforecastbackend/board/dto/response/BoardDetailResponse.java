package com.example.fashionforecastbackend.board.dto.response;

import com.example.fashionforecastbackend.board.domain.Board;

public record BoardDetailResponse(
	String text
) {
	public static BoardDetailResponse of(Board board) {
		return new BoardDetailResponse(board.getText());
	}
}
