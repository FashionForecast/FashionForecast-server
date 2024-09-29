package com.example.fashionforecastbackend.board.presentation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fashionforecastbackend.board.dto.request.BoardRequest;
import com.example.fashionforecastbackend.board.dto.response.BoardDetailResponse;
import com.example.fashionforecastbackend.board.dto.response.BoardListResponse;
import com.example.fashionforecastbackend.board.service.BoardService;
import com.example.fashionforecastbackend.global.response.ApiPageResponse;
import com.example.fashionforecastbackend.global.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
public class BoardController {
	private final BoardService boardService;

	@PostMapping
	public ApiResponse<Void> postBoard(@Valid @RequestBody BoardRequest boardRequest) {
		boardService.createInquiry(boardRequest);
		return ApiResponse.noContent();
	}

	@GetMapping("/{id}")
	public ApiResponse<BoardDetailResponse> getBoardDetail(@PathVariable("id") Long id) {
		BoardDetailResponse boardDetailResponse = boardService.getDetail(id);
		return ApiResponse.ok(boardDetailResponse);
	}

	@GetMapping
	public ApiPageResponse<BoardListResponse> getBoardList(
		@PageableDefault(sort = "id") Pageable pageable) {
		Page<BoardListResponse> boardResponse = boardService.getAll(pageable);
		return ApiPageResponse.ok(boardResponse);
	}
}
