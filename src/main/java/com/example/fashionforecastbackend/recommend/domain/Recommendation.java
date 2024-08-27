package com.example.fashionforecastbackend.recommend.domain;

import com.example.fashionforecastbackend.global.BaseTimeEntity;
import com.example.fashionforecastbackend.outfit.domain.Outfit;
import com.example.fashionforecastbackend.tempStage.domain.TempStage;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Recommendation extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "temp_stage_id")
	private TempStage tempStage;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "outfit_id")
	private Outfit outfit;
}
