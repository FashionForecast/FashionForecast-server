package com.example.fashionforecastbackend.region.domain.repository;

import com.example.fashionforecastbackend.region.domain.Region;
import java.util.Collection;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

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


        //MapSqlParameterSource에 담긴 키값이 컬럼과 매핑되고, values는 값과 매핑이된다.
        namedParameterJdbcTemplate.batchUpdate(sql, getRegionParameterSource(regions));

    }

    private MapSqlParameterSource[] getRegionParameterSource(Collection<Region> regions) {
        return regions.stream()
                .map(this::convertToSqlParameterSource)
                .toArray(MapSqlParameterSource[]::new);
    }

    /*
     * 키 - 값으로 매핑을 시킨다.
     * 키값은 위 쿼리문의 컬럼이름과 무조건 일치해야한다.
     */
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
