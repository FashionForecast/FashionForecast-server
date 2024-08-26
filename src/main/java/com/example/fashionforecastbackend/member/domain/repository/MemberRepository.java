package com.example.fashionforecastbackend.member.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fashionforecastbackend.member.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
}
