package com.example.fashionforecastbackend.weather.domain;

import java.util.Objects;

import com.example.fashionforecastbackend.global.BaseTimeEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(
	indexes = {
		@Index(name = "idx_base_date_time_nx_ny", columnList = "baseDate, baseTime, nx, ny")
	})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Weather extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 기준 날짜
	 */
	private String baseDate;
	/**
	 * 기준 시간
	 */
	private String baseTime;
	/**
	 * 습도
	 */
	private String reh;
	/**
	 * 온도
	 */
	private String tmp;
	/**
	 * 풍속
	 */
	private String wsd;

	/**
	 * 하늘 상태를 나타내는 enum 타입
	 */
	@Enumerated(EnumType.ORDINAL)
	private SkyStatus skyStatus;
	/**
	 * 강수 형태를 나타내는 enum 타입
	 */
	@Enumerated(EnumType.ORDINAL)
	private RainType rainType;
	/**
	 * 강수량
	 */
	private String rainfallAmount;
	/**
	 * 예보 날짜
	 */
	private String fcstDate;
	/**
	 * 예보 시간
	 */
	private String fcstTime;
	/**
	 * 위도
	 */
	private int nx;
	/**
	 * 경도
	 */
	private int ny;

	@Builder
	public Weather(
		String baseDate,
		String baseTime,
		String reh,
		String tmp,
		String wsd,
		String fcstDate,
		String fcstTime,
		SkyStatus skyStatus,
		RainType rainType,
		int nx,
		int ny
	) {
		this.baseDate = baseDate;
		this.baseTime = baseTime;
		this.reh = reh;
		this.tmp = tmp;
		this.wsd = wsd;
		this.fcstDate = fcstDate;
		this.fcstTime = fcstTime;
		this.skyStatus = skyStatus;
		this.rainType = rainType;
		this.nx = nx;
		this.ny = ny;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		if (!super.equals(o))
			return false;
		Weather that = (Weather)o;
		return Objects.equals(baseDate, that.baseDate) &&
			Objects.equals(baseTime, that.baseTime) &&
			Objects.equals(fcstDate, that.fcstDate) &&
			Objects.equals(fcstTime, that.fcstTime) &&
			Objects.equals(nx, that.nx) &&
			Objects.equals(ny, that.ny);
	}

	@Override
	public int hashCode() {
		return Objects.hash(baseDate, baseTime, fcstDate, fcstTime, nx, ny);
	}

}
