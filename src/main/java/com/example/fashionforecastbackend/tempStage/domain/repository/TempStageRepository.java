package com.example.fashionforecastbackend.tempStage.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.fashionforecastbackend.tempStage.domain.TempStage;

public interface TempStageRepository extends JpaRepository<TempStage, Long> {
	@Query("select t "
		+ "from TempStage t "
		+ "where t.level = ( "
		+ "select min(t2.level) "
		+ "from TempStage t2 "
		+ "where t2.minTemp <= :maxTemp "
		+ "and t2.maxTemp >= :minTemp"
		+ ")")
	Optional<TempStage> findMinByWeather(@Param("minTemp") int minTemp, @Param("maxTemp") int maxTemp);

	@Query("select t from TempStage t "
		+ "where t.level = ("
		+ "select max(t2.level) from TempStage t2 "
		+ "where t2.minTemp <= :maxTemp and t2.maxTemp >= :minTemp)")
	Optional<TempStage> findMaxByWeather(@Param("minTemp") int minTemp, @Param("maxTemp") int maxTemp);

	@Query("select t from TempStage t "
		+ "where t.level <= ("
		+ "select max(t2.level) - 1 from TempStage t2 "
		+ "where t2.minTemp <= :maxTemp and t2.maxTemp >= :minTemp) "
		+ "order by t.level")
	Optional<TempStage> findByWeatherAndCoolOption(@Param("minTemp") int minTemp, @Param("maxTemp") int maxTemp);

	@Query("select t from TempStage t "
		+ "where t.level <= ("
		+ "select min(t2.level) + 1 from TempStage t2 "
		+ "where t2.minTemp <= :maxTemp and t2.maxTemp >= :minTemp) "
		+ "order by t.level desc")
	Optional<TempStage> findByWeatherAndWarmOption(@Param("minTemp") int minTemp, @Param("maxTemp") int maxTemp);

}
