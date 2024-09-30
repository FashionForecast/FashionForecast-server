package com.example.fashionforecastbackend.board.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.fashionforecastbackend.board.domain.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
}
