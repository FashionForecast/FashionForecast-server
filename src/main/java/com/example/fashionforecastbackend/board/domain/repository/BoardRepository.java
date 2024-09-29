package com.example.fashionforecastbackend.board.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fashionforecastbackend.board.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

	Page<Board> findAllPage(Pageable pageable);
}
