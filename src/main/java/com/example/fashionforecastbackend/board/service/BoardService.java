package com.example.fashionforecastbackend.board.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.fashionforecastbackend.board.dto.request.BoardRequest;
import com.example.fashionforecastbackend.board.dto.response.BoardDetailResponse;
import com.example.fashionforecastbackend.board.dto.response.BoardListResponse;

public interface BoardService {
	void createInquiry(BoardRequest boardRequest);

	BoardDetailResponse getDetail(Long id);

	Page<BoardListResponse> getAll(Pageable pageable);
}
