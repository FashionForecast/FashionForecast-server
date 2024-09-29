package com.example.fashionforecastbackend.board.service.impl;

import static com.example.fashionforecastbackend.global.error.ErrorCode.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fashionforecastbackend.board.domain.Board;
import com.example.fashionforecastbackend.board.domain.repository.BoardRepository;
import com.example.fashionforecastbackend.board.dto.request.BoardRequest;
import com.example.fashionforecastbackend.board.dto.response.BoardDetailResponse;
import com.example.fashionforecastbackend.board.dto.response.BoardListResponse;
import com.example.fashionforecastbackend.board.service.BoardService;
import com.example.fashionforecastbackend.global.error.exception.NotFoundBoardException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
	private final BoardRepository boardRepository;

	@Transactional
	@Override
	public void createInquiry(final BoardRequest boardRequest) {
		Board board = Board.create(boardRequest.text());
		boardRepository.save(board);
	}

	@Override
	public BoardDetailResponse getDetail(Long id) {
		Board board = boardRepository.findById(id).orElseThrow(() -> new NotFoundBoardException(NOT_FOUND_BOARD));
		return BoardDetailResponse.of(board);
	}

	@Override
	public Page<BoardListResponse> getAll(Pageable pageable) {
		Page<Board> page = boardRepository.findAllPage(pageable);
		return page.map(BoardListResponse::of);
	}
}
