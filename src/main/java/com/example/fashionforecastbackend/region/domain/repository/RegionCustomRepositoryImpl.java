package com.example.fashionforecastbackend.region.domain.repository;

import java.util.Collection;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.example.fashionforecastbackend.region.domain.Region;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RegionCustomRepositoryImpl implements RegionCustomRepository {

	private static final int EXIST_REGION = 1;

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public void saveAll(Collection<Region> regions) {
		// MySql로 전환시 IGNORE 추가
		String sql = "INSERT INTO region (code, city, district, neighborhood, nx, ny)" +
			" VALUES (:code, :city, :district, :neighborhood, :nx, :ny)";

		namedParameterJdbcTemplate.batchUpdate(sql, getRegionParameterSource(regions));

	}

	private MapSqlParameterSource[] getRegionParameterSource(Collection<Region> regions) {
		return regions.stream()
			.map(this::convertToSqlParameterSource)
			.toArray(MapSqlParameterSource[]::new);
	}

	private MapSqlParameterSource convertToSqlParameterSource(Region region) {
		return new MapSqlParameterSource()
			.addValue("code", region.getAddress().getCode())
			.addValue("city", region.getAddress().getCity())
			.addValue("neighborhood", region.getAddress().getNeighborhood())
			.addValue("district", region.getAddress().getDistrict())
			.addValue("nx", region.getNx())
			.addValue("ny", region.getNy());
	}

	@Override
	public boolean isExist() {
		String sql = "SELECT EXISTS (SELECT 1 FROM region)";
		Integer result = namedParameterJdbcTemplate.queryForObject(sql, Map.of(), Integer.class);
		return result == EXIST_REGION;
	}
}
