package com.example.fashionforecastbackend.weather.domain.repository;

import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.example.fashionforecastbackend.weather.domain.Weather;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class WeatherCustomRepositoryImpl implements WeatherCustomRepository {

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public void saveAll(Collection<Weather> weathers) {

		String sql =
			"INSERT IGNORE INTO weather (base_date, base_time, reh, tmp, wsd, season, sky_status, rain_type, fcst_date, fcst_time,"
				+ "pcp, pop, nx, ny, created_at, modified_at)"
				+ " VALUES (:base_date, :base_time, :reh, :tmp, :wsd, :season, :sky_status, :rain_type, :fcst_date, :fcst_time,"
				+ ":pcp, :pop, :nx, :ny, :created_at, :modified_at)";

		namedParameterJdbcTemplate.batchUpdate(sql, getRegionParameterSource(weathers));

	}

	private MapSqlParameterSource[] getRegionParameterSource(Collection<Weather> weathers) {
		return weathers.stream().map(this::convertToSqlParameterSource).toArray(MapSqlParameterSource[]::new);
	}

	private MapSqlParameterSource convertToSqlParameterSource(Weather weather) {
		LocalDateTime now = LocalDateTime.now();
		return new MapSqlParameterSource().addValue("base_date", weather.getBaseDate())
			.addValue("base_time", weather.getBaseTime())
			.addValue("reh", weather.getReh())
			.addValue("tmp", weather.getTmp())
			.addValue("wsd", weather.getWsd())
			.addValue("fcst_date", weather.getFcstDate())
			.addValue("fcst_time", weather.getFcstTime())
			.addValue("pcp", weather.getPcp())
			.addValue("pop", weather.getPop())
			.addValue("season", weather.getSeason().name())
			.addValue("sky_status", weather.getSkyStatus().name())
			.addValue("rain_type", weather.getRainType().name())
			.addValue("nx", weather.getNx())
			.addValue("ny", weather.getNy())
			.addValue("created_at", now)
			.addValue("modified_at", now);
	}
}
