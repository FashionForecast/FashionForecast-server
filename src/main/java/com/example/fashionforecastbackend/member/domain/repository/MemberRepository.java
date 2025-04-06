package com.example.fashionforecastbackend.member.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.fashionforecastbackend.member.domain.Member;
import com.example.fashionforecastbackend.member.domain.constant.MemberJoinType;

public interface MemberRepository extends JpaRepository<Member, Long> {

	@Query("select m from Member m "
		+ "left join fetch m.memberOutfits "
		+ "where m.joinType = :joinType and m.socialId = :socialId")
	Optional<Member> findByJoinTypeAndSocialId(@Param("joinType") MemberJoinType joinType,
		@Param("socialId") String socialId);

	boolean existsByJoinTypeAndSocialId(MemberJoinType joinType, String socialId);

	boolean existsBySocialId(final String socialId);

	@Modifying
	void deleteById(final Long memberId);

}
