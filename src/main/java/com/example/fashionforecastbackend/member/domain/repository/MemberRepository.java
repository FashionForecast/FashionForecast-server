package com.example.fashionforecastbackend.member.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.example.fashionforecastbackend.member.domain.Member;
import com.example.fashionforecastbackend.member.domain.constant.MemberJoinType;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByJoinTypeAndSocialId(MemberJoinType joinType, String socialId);

	boolean existsByJoinTypeAndSocialId(MemberJoinType joinType, String socialId);

	boolean existsBySocialId(final String socialId);

	@Modifying
	void deleteById(final Long memberId);

}
