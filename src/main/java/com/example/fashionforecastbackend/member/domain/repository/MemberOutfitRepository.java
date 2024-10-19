package com.example.fashionforecastbackend.member.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.fashionforecastbackend.member.domain.MemberOutfit;

public interface MemberOutfitRepository extends JpaRepository<MemberOutfit, Long> {

	@Query("SELECT mo FROM MemberOutfit mo JOIN FETCH mo.tempStage ts "
		+ "WHERE mo.member.id = :memberId AND mo.isDeleted = false ORDER BY ts.level ASC")
	List<MemberOutfit> findByMemberIdWithTempStage(@Param("memberId") final Long memberId);

	@Query("SELECT mo FROM MemberOutfit mo "
		+ "WHERE mo.member.id = :memberId AND mo.tempStage.id = :tempStageId AND mo.isDeleted = false")
	List<MemberOutfit> findByMemberIdAndTempStageId(@Param("memberId") final Long memberId,
		@Param("tempStageId") final Long tempStageId);

	@Query("SELECT mo FROM MemberOutfit mo "
		+ "WHERE mo.member.id = :memberId AND mo.tempStage.id = :tempStageId AND mo.isDeleted = false")
	Integer countByTempStageIdAndMemberId(final @Param("tempStageId") Long tempStageId,
		final @Param("memberId") Long memberId);

	@Modifying
	void deleteAllByMemberId(final Long memberId);
}
