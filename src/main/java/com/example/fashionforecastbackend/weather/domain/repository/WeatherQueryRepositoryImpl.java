package com.example.fashionforecastbackend.weather.domain.repository;

import static com.example.fashionforecastbackend.weather.domain.QWeather.*;

import java.util.List;

import com.example.fashionforecastbackend.weather.domain.Weather;
import com.example.fashionforecastbackend.weather.dto.request.WeatherFilter;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WeatherQueryRepositoryImpl implements WeatherQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Weather> findWeather(WeatherFilter weatherFilter) {
		return queryFactory
			.selectFrom(weather)
			.where(
				weather.baseDate.eq(weatherFilter.baseDate()),
				weather.baseTime.eq(weatherFilter.baseTime()),
				weather.nx.eq(weatherFilter.nx()),
				weather.ny.eq(weatherFilter.ny()),
				weather.fcstDate.concat(weather.fcstTime).goe(weatherFilter.startDate() + weatherFilter.startTime()),
				weather.fcstDate.concat(weather.fcstTime).loe(weatherFilter.endDate() + weatherFilter.endTime())
			)
			.orderBy(weather.fcstDate.asc(), weather.fcstTime.asc())
			.fetch();
	}

	@Override
	public void deletePastWeathers(String nowBaseDateTime) {
		queryFactory
			.delete(weather)
			.where(weather.baseDate.concat(weather.baseTime).lt(nowBaseDateTime))
			.execute();
	}

}
