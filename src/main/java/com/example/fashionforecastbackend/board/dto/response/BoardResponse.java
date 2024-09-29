package com.example.fashionforecastbackend.board.dto.response;

import com.example.fashionforecastbackend.board.domain.Board;

public record BoardResponse(
	String text
) {
	public static BoardResponse of(Board board) {
		return new BoardResponse(board.getText());
	}
}
