package com.example.fashionforecastbackend.recommend.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.fashionforecastbackend.recommend.domain.Recommendation;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

	@Query("select r from Recommendation r "
		+ "join fetch r.outfit "
		+ "where r.tempStage.id = :tempStageId")
	List<Recommendation> findByTempStage(@Param("tempStageId") Long tempStageId);
}
