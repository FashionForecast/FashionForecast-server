package com.example.fashionforecastbackend.member.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fashionforecastbackend.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUsername(String username);
}
