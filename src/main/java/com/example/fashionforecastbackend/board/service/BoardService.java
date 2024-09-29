package com.example.fashionforecastbackend.board.service;

import com.example.fashionforecastbackend.board.dto.request.BoardRequest;
import com.example.fashionforecastbackend.board.dto.response.BoardResponse;

public interface BoardService {
	void createInquiry(BoardRequest boardRequest);

	BoardResponse boardDetail(Long id);
}
