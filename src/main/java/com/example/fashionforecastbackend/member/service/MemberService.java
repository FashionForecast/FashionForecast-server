package com.example.fashionforecastbackend.member.service;

import com.example.fashionforecastbackend.member.domain.Member;

import java.util.Optional;

public interface MemberService {
    Optional<Member> findByUsername(String username);
    void save(Member member);
}
