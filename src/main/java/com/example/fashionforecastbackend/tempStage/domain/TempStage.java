package com.example.fashionforecastbackend.tempStage.domain;

import java.util.ArrayList;
import java.util.List;

import com.example.fashionforecastbackend.global.BaseTimeEntity;
import com.example.fashionforecastbackend.recommend.domain.Recommendation;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

	private int level;

	private int minTemp;

	private int maxTemp;

	@OneToMany(mappedBy = "tempStage")
	private List<Recommendation> recommendations = new ArrayList<>();
}
