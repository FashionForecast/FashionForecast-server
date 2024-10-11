package com.example.fashionforecastbackend.member.domain.repository;

import java.util.LinkedList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.fashionforecastbackend.member.domain.MemberOutfit;

public interface MemberOutfitRepository extends JpaRepository<MemberOutfit, Long> {

	@Query("SELECT mo FROM MemberOutfit mo JOIN FETCH mo.tempStage ts WHERE mo.member.id = :memberId ORDER BY ts.level ASC")
	LinkedList<MemberOutfit> findByMemberIdWithTempStage(@Param("memberId") final Long memberId);

}
