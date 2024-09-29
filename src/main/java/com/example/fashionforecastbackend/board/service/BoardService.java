package com.example.fashionforecastbackend.board.service;

import com.example.fashionforecastbackend.board.dto.request.BoardRequest;

public interface BoardService {
	void createInquiry(BoardRequest boardRequest);
}
