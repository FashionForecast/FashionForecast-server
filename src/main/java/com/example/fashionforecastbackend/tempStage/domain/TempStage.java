package com.example.fashionforecastbackend.tempStage.domain;

import com.example.fashionforecastbackend.global.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TempStage extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private int level;

	private int minTemp;

	private int maxTemp;

	private TempStage(final int level, final int minTemp, final int maxTemp) {
		this.level = level;
		this.minTemp = minTemp;
		this.maxTemp = maxTemp;
	}

	public static TempStage create(final int level, final int minTemp, final int maxTemp) {
		return new TempStage(level, minTemp, maxTemp);
	}
}
