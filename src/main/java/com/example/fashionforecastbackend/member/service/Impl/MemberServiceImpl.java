package com.example.fashionforecastbackend.member.service.Impl;

import com.example.fashionforecastbackend.member.domain.Member;
import com.example.fashionforecastbackend.member.domain.repository.MemberRepository;
import com.example.fashionforecastbackend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    public Optional<Member> findByUsername(String username) {
        Optional<Member> member = memberRepository.findByUsername(username);

        if (member.isEmpty()) {
            log.error("해당 회원은 존재하지 않습니다. : {}", username);
            // Optional.empty() 반환
            return Optional.empty();
        }
        // Optional.of()를 사용하여 존재하는 회원을 감싸 반환
        return member;
    }

    @Transactional
    @Override
    public void save(Member member) {
        memberRepository.save(member);
    }

}
