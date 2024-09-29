package com.example.fashionforecastbackend.board.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fashionforecastbackend.board.domain.Board;
import com.example.fashionforecastbackend.board.domain.repository.BoardRepository;
import com.example.fashionforecastbackend.board.dto.request.BoardRequest;
import com.example.fashionforecastbackend.board.service.BoardService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
	private final BoardRepository boardRepository;

	@Override
	public void createInquiry(final BoardRequest boardRequest) {
		Board board = Board.create(boardRequest.text());
		boardRepository.save(board);
	}
}
