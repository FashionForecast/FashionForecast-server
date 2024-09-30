package com.example.fashionforecastbackend.board.dto.response;

import com.example.fashionforecastbackend.board.domain.Board;

public record BoardListResponse(
	Long id,
	String text
) {
	public static BoardListResponse of(Board board) {
		return new BoardListResponse(board.getId(), board.getText());
	}
}
