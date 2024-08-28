package com.example.fashionforecastbackend.tempStage.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.fashionforecastbackend.tempStage.domain.TempStage;

@Repository
public interface TempStageRepository extends JpaRepository<TempStage, Long> {

	@Query("select t "
		+ "from TempStage t "
		+ "where t.maxTemp >= :temp and t.minTemp <= :temp")
	Optional<TempStage> findByWeather(@Param("temp") int temp);

	@Query("select t1 "
		+ "from TempStage t1 "
		+ "where t1.level = ( "
		+ "		select t2.level + 1 from TempStage t2 "
		+ "     where t2.minTemp <= :temp and t2.maxTemp >= :temp "
		+ ") ")
	Optional<TempStage> findByWeatherAndCoolOption(@Param("temp") int temp);

	@Query("select t1 "
		+ "from TempStage t1 "
		+ "where t1.level = ( "
		+ "		select t2.level - 1 from TempStage t2 "
		+ "     where t2.minTemp <= :temp and t2.maxTemp >= :temp "
		+ ") ")
	Optional<TempStage> findByWeatherAndWarmOption(@Param("temp") int temp);

	Optional<TempStage> findByLevel(int level);
}
