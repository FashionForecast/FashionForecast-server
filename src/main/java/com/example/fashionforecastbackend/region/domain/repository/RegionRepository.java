package com.example.fashionforecastbackend.region.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.fashionforecastbackend.region.domain.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long>, RegionCustomRepository {

	@Query("SELECT r FROM Region r WHERE r.address.city = :city AND r.address.district = :district AND r.address.neighborhood = :neighborhood")
	Optional<Region> findByAddress(@Param("city") String city, @Param("district") String district,
		@Param("neighborhood") String neighborhood);

	Optional<Region> findByNxAndNy(double nx, double ny);

	@Query("SELECT r FROM Region r WHERE r.address.city IN :cities")
	List<Region> findByCities(@Param("cities") List<String> cities);
}
