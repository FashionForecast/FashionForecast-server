package com.example.fashionforecastbackend.outfit.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.fashionforecastbackend.outfit.domain.Outfit;
import com.example.fashionforecastbackend.outfit.domain.OutfitType;

@Repository
public interface OutfitRepository extends JpaRepository<Outfit, Long> {

	@Query("select o from Outfit o "
		+ "where o.outfitType = :type")
	Optional<Outfit> findByUmbrellaType(@Param("type") OutfitType type);
}