package com.example.fashionforecastbackend.board.presentation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fashionforecastbackend.board.dto.request.BoardRequest;
import com.example.fashionforecastbackend.board.service.BoardService;
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
}
